package ca.rdmss.multitest;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import ca.rdmss.util.UtilAnnotation;

public class MultiTestRule implements TestRule {

	final private MultiHelper helper; 

	public MultiTestRule() {
		this.helper = new MultiHelper();
	}

	public MultiTestRule(Object testInstance) {
		this.helper = new MultiHelper(testInstance);
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

			// Run jobs in each set of threads
			for(String str: tsets){
				int threadNo = Integer.parseInt(str);
				
				helper.runCycle(tsets.length, repeatNo, threadNo,  testClass, newInstance, jobs, endOfCycle);
				
				if( helper.stopCycle ){
					break;
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
}
