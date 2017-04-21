package ca.rdmss.multitest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import ca.rdmss.util.UtilTimer;

public class MultiTestHelper {

	public static String start(Object obj, int... repeatForThreads) throws InterruptedException, InstantiationException, IllegalAccessException {
		return start(obj.getClass(), obj, repeatForThreads);
	}

	public static String start(Object obj) throws InterruptedException, InstantiationException, IllegalAccessException {
		return start(obj.getClass(), obj, null);
	}

	public static String start(final Class<? extends Object> clazz, int... repeatForThreads)
			throws InterruptedException, InstantiationException, IllegalAccessException {
		return start(clazz, clazz.newInstance(), repeatForThreads);
	}

	public static String start(final Class<? extends Object> clazz)
			throws InterruptedException, InstantiationException, IllegalAccessException {
		return start(clazz, clazz.newInstance(), null);
	}

	private static String start(final Class<? extends Object> clazz, Object obj, int[] repeatForThreads)
			throws InterruptedException, InstantiationException, IllegalAccessException {
		String result = "";

		if (clazz.isAnnotationPresent(MultiTest.class)) {
			
			// for proper reporting
			String timeUnit = null;
			double timeScale = 0;
			
			MultiTest testcase = (MultiTest) clazz.getAnnotation(MultiTest.class);

			final int maxtry = testcase.repeatNo();
			final boolean isNewInstance = (testcase.newInstance() == NewInstance.True);

			int[] repeats = (repeatForThreads == null)? new int[]{1}: repeatForThreads;

			for(int howManyThreadsRepeat: repeats ){
			
				int howManyThreadsTtl = 0;
	
				// collect threaded methods
	
				List<Method> methodlist = new ArrayList<Method>(12);
				List<MultiThread> threadlist = new ArrayList<MultiThread>(12);
				List<Integer> howManylist = new ArrayList<Integer>(12);
	
				// check if we have thread which must be run after all other ends
	
				final AtomicReference<Method> report = new AtomicReference<Method>();
	
				for (Method method : clazz.getDeclaredMethods()) {
	
					if (method.isAnnotationPresent(MultiThread.class)) {
	
						MultiThread newthread = (MultiThread) method.getAnnotation(MultiThread.class);
						
						int howMany = Integer.parseInt(newthread.threadSet().split(",")[0]); // TODO: no null check
	
						threadlist.add(newthread);
						methodlist.add(method);
						howManylist.add(howMany);
	
						howManyThreadsTtl += (repeatForThreads == null)? howMany: howManyThreadsRepeat;
						
					} else if (method.isAnnotationPresent(Report.class)) {
						
						if(report.get() == null ){
							report.set(method);
						} else {
							throw new RuntimeException("@Report annotation must be specified once");
						}
					}
				}
	
				final boolean isReport = report.get() != null;
	
				// prepare threads and count runs against phaser
	
				final AtomicInteger counter = new AtomicInteger(0);
	
				final int maxthreads = howManyThreadsTtl;
	
				final Thread[] threads = new Thread[maxthreads];
				final Phaser phaser = new Phaser(maxthreads);
	
				final AtomicReference<Object> atomicReference = new AtomicReference<Object>();
	
				for (int i=0,t=0, maxi=methodlist.size(); i < maxi; i++) {
	
					final Method method = methodlist.get(i);
					final int howMany = howManylist.get(i);
	
					for(int j=0,maxj=(repeatForThreads == null)? howMany: howManyThreadsRepeat; j<maxj; j++){
						Thread thread = new Thread(new Runnable() {
		
							@Override
							public void run() {
								for (int m=0; m < maxtry; m++) {
		
									try {
										method.invoke(atomicReference.get());
									} catch (Throwable e) {
										e.printStackTrace();
									}
		
									int cnt = counter.get();
		
									// report thread must be run after all other ends
		
									if (isReport) {
										if (cnt % maxthreads == 0) {
											try {
												report.get().invoke(atomicReference.get());
											} catch (Throwable e) {
												e.printStackTrace();
											}
										}
									}
		
									// create new instance if required before next run
		
									if (isNewInstance) {
										if (cnt % maxthreads == 0) {
											try {
												atomicReference.set(clazz.newInstance());
											} catch (Throwable e) {
												e.printStackTrace();
											}
										}
									}
		
									// wait for all threads here
									counter.incrementAndGet();
									phaser.arriveAndAwaitAdvance();
								}
							}
		
						});
						threads[t++] = thread;
					}
				}
	
				final long start = System.currentTimeMillis();
	
				// start threads here
	
				atomicReference.set(obj);
	
				for (Thread thread : threads) {
					thread.start();
				}
	
				// wait for all done
	
				for (int max=maxthreads*maxtry; counter.get() < max; Thread.sleep(10L))
					;
	
				final double ttlmls = System.currentTimeMillis() - start;
	
				if( repeatForThreads == null ){
					// Single line report
					result += String.format("%s=== %50s done %,d time(s) in %5.1f %3s (%5.1f %3s/try) === ", 
							result.isEmpty()?"": "\n",
							clazz.getSimpleName(), maxtry, 
							UtilTimer.timeValMls(ttlmls), UtilTimer.timeUnitMls(ttlmls),
							UtilTimer.timeValMls(ttlmls / maxtry), UtilTimer.timeUnitMls(ttlmls / maxtry));
				} else {
					
					// Initialize timeUnit once - from first run
					if( timeUnit == null ){
						timeUnit = UtilTimer.timeUnitMls(ttlmls / maxtry);
						timeScale = UtilTimer.timeScaleMls(ttlmls / maxtry);
					}
						
					// Table
					result += String.format("\n%-7d %-5.1f  %3s %-5.1f  %3s %10.3f", 
							howManyThreadsRepeat,
							UtilTimer.timeValMls(ttlmls), UtilTimer.timeUnitMls(ttlmls),
							UtilTimer.timeValMls(ttlmls / maxtry), UtilTimer.timeUnitMls(ttlmls / maxtry),
							(ttlmls / maxtry)*timeScale
						);
				}
			}
			
			// Add header
			if( repeatForThreads != null ){
				result = String.format("\n=== %s done %,d time(s) ===\n%-7s %-9s %-11s %s(%s)\n------- ---------- ---------- ----------", 
						clazz.getSimpleName(), maxtry, 
						"Threads", "Total", " OneTry", "OneTry", timeUnit)
						+ result + "\n------- ---------- ---------- ----------";
			}
			
		} else {
			throw new RuntimeException(String.format("No annotation @%s presented for class %s",
					MultiTest.class.getName(), clazz.getName()));
		}

		return result;
	}

}
