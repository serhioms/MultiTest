package ca.rdmss.multitest.junitrule;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import ca.rdmss.multitest.annotation.MultiBefore;
import ca.rdmss.multitest.annotation.MultiEndOfCycle;
import ca.rdmss.multitest.annotation.MultiEndOfSet;
import ca.rdmss.multitest.annotation.MultiTest;
import ca.rdmss.multitest.annotation.MultiThread;
import ca.rdmss.util.UtilAnnotation;
import ca.rdmss.util.UtilTimer;

public class MultiTestRule implements TestRule {

	final private MultiHelper helper; 
	public double ttlmls;
	
	public MultiTestRule() {
		helper = new MultiHelper();
	}

	public MultiTestRule(Object testInstance) {
		helper = new MultiHelper(testInstance);
	}

	@Override
	public Statement apply(Statement base, Description description) {
		try {
			Class<?> testClass = description.getTestClass();
	
			// Same as MultiTest defaults 
			int repeatNo = 1;
			String threadSet = "1";
			boolean newInstance = false;

			// Read actuals if presented
			MultiTest classConfig = testClass.getAnnotation(MultiTest.class);
			if( classConfig != null ){
				newInstance = classConfig.newInstance();
				threadSet = classConfig.threadSet();
				repeatNo = classConfig.repeatNo();
			} 

			// Get jobs
			List<Method> jobs = UtilAnnotation.getAnnotatedMethods(testClass, MultiThread.class);

			// Get end of cycle job 
			Method endOfCycle = UtilAnnotation.getAnnotatedMethod(testClass, MultiEndOfCycle.class);

			// Get thread sets
			String[] tsets = threadSet.split(",");

			// Get end of set 
			Method endOfSet = UtilAnnotation.getAnnotatedMethod(testClass, MultiEndOfSet.class);

			
			// Get and start @MultiBefore first 
			Method before = UtilAnnotation.getAnnotatedMethod(testClass, MultiBefore.class);
			if (before != null) {
				before.invoke(helper.testInstance);
			}

			// Run jobs in each set of threads
			for(String str: tsets){
				int threadNo = Integer.parseInt(str);

				long start = System.currentTimeMillis();

				boolean isDone = helper.runCycle(tsets.length, repeatNo, threadNo,  testClass, newInstance, jobs, endOfCycle);
				
				// set method must be run at the end of set
				if (endOfSet != null) {
					try {
						endOfSet.invoke(helper.testInstance);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
				
				long finish = System.currentTimeMillis();
				ttlmls = finish - start;
	
				// report
				if (helper.seriesNo == 1) {
					// Single line report
					helper.result += String.format("%s=== %50s done %,d time(s) in %.1f %s (%.1f %3s/try) ===",
							helper.result.isEmpty() ? "" : "\n", testClass.getSimpleName(), repeatNo, UtilTimer.timeValMls(ttlmls),
							UtilTimer.timeUnitMls(ttlmls), UtilTimer.timeValMls(ttlmls / repeatNo),
							UtilTimer.timeUnitMls(ttlmls / repeatNo));
				} else {
	
					// Initialize timeUnit once - from first run
					if (helper.timeUnit == null) {
						helper.timeUnit = UtilTimer.timeUnitMls(ttlmls / repeatNo);
						helper.timeScale = UtilTimer.timeScaleMls(ttlmls / repeatNo);
					}
	
					// Table
					helper.result += String.format("\n%-7d %-5.1f  %3s %-5.1f  %3s %10.3f", helper.threadNo, UtilTimer.timeValMls(ttlmls),
							UtilTimer.timeUnitMls(ttlmls), UtilTimer.timeValMls(ttlmls / repeatNo),
							UtilTimer.timeUnitMls(ttlmls / repeatNo), (ttlmls / repeatNo) * helper.timeScale);
					helper.result += helper.addresult;
					helper.addresult = "";
				}
				
				if( isDone ){
					break;
				}
			}
		} catch(Throwable t){
			t.printStackTrace();
		}
		return base;
	}

	public String getReport() {
		return helper.getReport();
	}

	public String getResult() {
		return helper.result;
	}

	public boolean isTable() {
		return helper.seriesNo > 1;
	}

	public int getThreadNo() {
		return helper.threadNo;
	}

	public void addFailed(int expected, int actual) {
		helper.addResultFailed(expected, actual);
	}

	public void addPass(int expected, int actual) {
		helper.addResultPass(expected, actual);
	}

	public boolean isFailed() {
		return helper.isFailed;
	}
}
