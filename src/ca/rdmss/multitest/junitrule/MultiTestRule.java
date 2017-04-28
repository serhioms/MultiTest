package ca.rdmss.multitest.junitrule;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import ca.rdmss.multitest.MultiHelper;
import ca.rdmss.multitest.annotation.MultiBefore;
import ca.rdmss.multitest.annotation.MultiEndOfCycle;
import ca.rdmss.multitest.annotation.MultiEndOfSet;
import ca.rdmss.multitest.annotation.MultiTest;
import ca.rdmss.multitest.annotation.MultiThread;
import ca.rdmss.util.UtilAnnotation;

public class MultiTestRule implements TestRule {

	final private MultiHelper helper; 

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
				if( helper.runCycle(tsets.length, repeatNo, threadNo,  testClass, newInstance, jobs, endOfCycle) ){
					break;
				}
				// cycle method must be run at the end of cycle

				if (endOfSet != null) {
					try {
						endOfSet.invoke(helper.testInstance);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
		} catch(Throwable t){
			t.printStackTrace();
		}
		return base;
	}

	public String getResult() {
		return helper.getResult();
	}

	public boolean isTable() {
		return helper.seriesNo > 1;
	}

	public int getThreadNo() {
		return helper.threadNo;
	}
}
