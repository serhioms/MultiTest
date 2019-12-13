package ca.rdmss.multitest.junitrule;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiHelper {

	final static public boolean CONTINUE = false;
	final static public boolean EXIT = true;

	Object testInstance;

	AtomicInteger invokeCounter = new AtomicInteger(0);
	AtomicInteger threadCounter = new AtomicInteger(0);
	
	// Report
	String result = "";
	String addresult = "";
	String timeUnit = null;
	double timeScale = 0;

	// last run
	boolean isFailed;
	int seriesNo;
	int repeatNo;
	Class<?> testClass;
	int threadNo;

	public MultiHelper() {
		this.testInstance = null;
	}

	public MultiHelper(Object testInstance) {
		this.testInstance = testInstance;
	}

	public boolean runCycle(int _seriesNo, int _repeatNo, int _threadNo, Class<?> _testClass, final boolean newInstance,
			final List<Method> jobs, final Method endOfCycle) throws Throwable {

		isFailed = false;
		
		invokeCounter.set(0);
		threadCounter.set(0);
		
		// Need for reports etc
		this.seriesNo = _seriesNo;
		this.repeatNo = _repeatNo;
		this.threadNo = _threadNo;
		this.testClass = _testClass;

		final int maxthreads = threadNo * jobs.size();

		final Phaser phaser = new Phaser(maxthreads);
		
		Thread[] threads = new Thread[maxthreads];
		
		for (int h = 0, t = 0; h < threadNo; h++) {
			for (int r = 0, maxr = jobs.size(); r < maxr; r++) {

				final Method job = jobs.get(r);

				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
						int cnt = -1;

						for (int n = 0; n < repeatNo; n++) {
							try {
								// run task
								job.invoke(testInstance);
								// count task done
								cnt = invokeCounter.incrementAndGet();
							} catch (Throwable e) {
								e.printStackTrace();
								return;
							}

							// cycle method must be run at the end of cycle

							if (endOfCycle != null) {
								if (cnt % maxthreads == 0) { 
									// catch last thread
									try {
										endOfCycle.invoke(testInstance);
									} catch (Throwable e) {
										e.printStackTrace();
										return;
									}
								}
							}

							// create new instance if required before next cycle

							if (newInstance) {
								if (cnt % maxthreads == 0) { 
									// catch last thread
									try {
										testInstance = testClass.newInstance();
									} catch (Throwable e) {
										e.printStackTrace();
										return;
									}
								}
							}

							// wait for all threads here
							phaser.arriveAndAwaitAdvance();
						}
						
						threadCounter.incrementAndGet();
					}
				}, String.format("%s.%s", job.getName(), h));
				threads[t++] = thread;
			}
		}

		// check for instance

		if (testInstance == null) {
			testInstance = testClass.newInstance();
		}

		// start threads here
		for (Thread thread : threads) {
			thread.start();
		}

		// wait for all done

		for (int max=maxthreads; threadCounter.get() < max; )
			TimeUnit.NANOSECONDS.sleep(50L);

		return invokeCounter.get() != (maxthreads * repeatNo);
	}

	public String getReport() {
		if (seriesNo == 1) {
			return result;
		} else {
			// Add header for series of threads
			return String.format(
					  "\n=== %s done %,d time(s) ===" + "\n%-7s %-9s %-11s %s(%s)"
					+ "\n------- ---------- ---------- ----------",
					testClass.getSimpleName(), repeatNo, "Threads", "Total", " OneTry", "OneTry", timeUnit) 
					+ result
					+ "\n------- ---------- ---------- ----------";
		}
	}

	private void addResult(String str, boolean isFailed) {
		addresult += str;
	}

	public void addResultFailed(int expected, int actual) {
		addResult(String.format(" %s: expected=%,d actual=%,d", "failed", expected, actual), true);
	}

	public void addResultPass(int expected, int actual) {
		addResult(String.format(" %s: expected=%,d actual=%,d", "pass", expected, actual), isFailed);
	}

}
