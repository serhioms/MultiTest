package ca.rdmss.multitest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import ca.rdmss.util.UtilTimer;

public class MultiTest {

	public static String start(Object obj) throws InterruptedException, InstantiationException, IllegalAccessException {
		return start( obj.getClass(), obj);
	}

	public static String start(final Class<? extends Object> clazz) throws InterruptedException, InstantiationException, IllegalAccessException {
		return start( clazz, null);
	}

	
	private static String start(final Class<? extends Object> clazz, Object obj) throws InterruptedException, InstantiationException, IllegalAccessException {
		String result = null;
		
		if( clazz.isAnnotationPresent( ConcurrentTest.class) ){
			
			ConcurrentTest testcase = (ConcurrentTest )clazz.getAnnotation(ConcurrentTest.class);
			
			final int maxtry = testcase.maxTry();
			final boolean isNewInstance = testcase.newInstancePerTry();
			
			// collect threaded methods
			
			List<Method> methodlist = new ArrayList<Method>(12);
			List<NewThread> threadlist = new ArrayList<NewThread>(12);
			
			// check if we have thread which must be run after all other ends 
			
			final AtomicReference<Method> lastmethod = new AtomicReference<Method>();
			
			for( Method method: clazz.getDeclaredMethods() ){
				
				if( method.isAnnotationPresent(NewThread.class)){
					
					NewThread newthread = (NewThread )method.getAnnotation(NewThread.class);
					
					boolean isLast = newthread.runLast();
					
					if( isLast ){
						lastmethod.set(method);
					} else {
						threadlist.add(newthread);
						methodlist.add(method);
					}
					
				}
			}
			
			final boolean isLast = lastmethod.get() != null;
			
			// prepare threads and count runs against phaser
			
			final AtomicInteger counter = new AtomicInteger(0);
			
			final int maxthreads = methodlist.size();
			
			Thread[] threads = new Thread[maxthreads];
					
			final Phaser phaser = new Phaser(maxthreads);
			
			final AtomicReference<Object> atomicReference = new AtomicReference<Object>();
			
			for(int i=0; i<maxthreads; i++){
				
				final Method method = methodlist.get(i);
				
				Thread thread = new Thread(new Runnable(){

					@Override
					public void run() {
						for( int t=0; t<maxtry; t++){
							
							try {
								method.invoke(atomicReference.get());
							} catch( Throwable e){
								e.printStackTrace();
							}
							
							int cnt = counter.get();
							
							// one thread must be run after  all other ends
							
							if( isLast ){
								if( cnt % maxthreads == 0 ){
									try {
										lastmethod.get().invoke(atomicReference.get());
									} catch( Throwable e){
										e.printStackTrace();
									}
								}
							}
							
							// create new instance if required before next run
							
							if( isNewInstance ){
								if( cnt % maxthreads == 0 ){
									try {
										atomicReference.set( clazz.newInstance());
									} catch( Throwable e){
										e.printStackTrace();
									}
								}
							}
							
							// wait for all threads here then start them all simultaneously
							counter.incrementAndGet();
							phaser.arriveAndAwaitAdvance();
						}
					}
					
				});
				
				threads[i] = thread;
			}
			
			final long start = System.currentTimeMillis();
			
			// start threads here
			
			atomicReference.set(obj == null? clazz.newInstance(): obj);
			
			for(Thread thread: threads){
				thread.start();
			}
			
			// wait for all done
			
			for(int max = maxthreads*maxtry; counter.get() < max; Thread.sleep(10L));
			
			final double ttlmls = System.currentTimeMillis() - start;
			
			result = String.format("=== %50s done %,d time(s) in %5.1f %3s (%5.1f %3s/try) === ",
					clazz.getSimpleName(), maxtry,
					UtilTimer.timeValMls(ttlmls), UtilTimer.timeScaleMls(ttlmls),
					UtilTimer.timeValMls(ttlmls/maxtry), UtilTimer.timeScaleMls(ttlmls/maxtry)
					);
			
		} else {
			throw new RuntimeException(String.format("No annotation @%s presened for class %s", ConcurrentTest.class.getName(), clazz.getName()));
		}
		
		return result;
	}
	
	
	
}
