package ca.rdmss.multitest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class MultiTestRule implements TestRule {

	final static private boolean CONTINUE = false;
	final static private boolean EXIT = true;
	final static public boolean DEMO = true;
	
	volatile private Object testInstance = null;
	volatile private boolean stopCycle = false; 

	final private boolean isDemo;
	
	public MultiTestRule() {
		this(false);
	}

	public MultiTestRule(boolean isDemo) {
		this.isDemo = isDemo;
	}

	final static private Statement doNothing = new Statement(){
		@Override
		public void evaluate() throws Throwable {
		}
	};
	
	@Override
	public Statement apply(final Statement base, Description description) {
		try {
			Class<?> testClass = description.getTestClass();
	
			ExecutionOrder execute = ExecutionOrder.Sequentially;
			Runnable[] jobs = null;
			
			NewInstance newInstance = NewInstance.NA;
			String threadSet = null;
			int repeatNo = 0;
			
			MultiTest classConfig = testClass.getAnnotation(MultiTest.class);
			if( classConfig != null ){
				newInstance = classConfig.newInstance();
				threadSet = classConfig.threadSet();
				repeatNo = classConfig.repeatNo();
				execute = classConfig.execute();
			}
			
			MultiThread methodConfig = description.getAnnotation(MultiThread.class);
			if( execute == ExecutionOrder.Sequentially ){
				if( methodConfig != null ){
					if( newInstance == NewInstance.NA){
						newInstance = methodConfig.newInstance();
					} else if( methodConfig.newInstance() != NewInstance.NA){
						threadSet = methodConfig.threadSet(); 
					}
					if( threadSet == null || threadSet.isEmpty() ){
						threadSet = methodConfig.threadSet(); 
					} else if( methodConfig.threadSet() != null && !methodConfig.threadSet().isEmpty() ){
						threadSet = methodConfig.threadSet(); 
					}
					if( repeatNo == 0 ){
						repeatNo = methodConfig.repeatNo();
					} else if( methodConfig.repeatNo() > 0 ){
						repeatNo = methodConfig.repeatNo();
					}
				}
			}
			
			String methodName = description.getMethodName();
			
			// Check for parallel configuration
			if( threadSet == null || threadSet.isEmpty() ){
				throw new RuntimeException(String.format("@%s(threadSet=\"\") %s must not be empty to run %s!", 
						execute==ExecutionOrder.Parallel?"MultiTest":"MultiThread",
						execute==ExecutionOrder.Parallel?"":methodName,
						execute));
			}

			if( repeatNo == 0 ){
				throw new RuntimeException(String.format("@%s(repeatNo=\"\") %s must not be empty to run %s!", 
						execute==ExecutionOrder.Parallel?"MultiTest":"MultiThread",
						execute==ExecutionOrder.Parallel?"":methodName,
						execute));
			}

			// Final initialization must not be N/A
			if( newInstance == NewInstance.NA ){
				newInstance = NewInstance.False;
			}
			
			// initialize jobs
			if( execute  == ExecutionOrder.Sequentially ){
				jobs = new Runnable[]{new Runnable(){
					@Override
					public void run() {
						try {
							base.evaluate();
						} catch (Throwable e) {
							e.printStackTrace();
							stopCycle = true;
						}
					}
				}};
			} else if( execute  == ExecutionOrder.Parallel ){
				jobs = collectAllTestMethods(testClass);
			}

			//String methodName = description.getMethodName();
			
			// Same instance or new all the time
			if( newInstance == NewInstance.False ){
				testInstance = testClass.newInstance();
			}

			// Run jobs in threads
			String[] threads = threadSet.split(",");
			for(String str: threads){
				int threadNo = Integer.parseInt(str);
				if( runCycle(repeatNo, testClass, newInstance == NewInstance.True, threadNo, jobs) ){
					break;
				}
			}
		} catch(Throwable t){
			t.printStackTrace();
		}
		return doNothing;
	}

	private boolean runCycle(int repeatNo, Class<?> testClass, boolean isNew, int threadNo, Runnable[] runnables) throws Throwable {
		for(int i=0; i<repeatNo; i++){
			
			if( isDemo ){
				System.out.print(i+1);
				System.out.print(") ");
				
				if( isNew ){ // create new instance before each cycle
					testInstance = testClass.newInstance();
				}
				
				for(Runnable job: runnables){
					job.run();
				}
				System.out.printf("(%s%s):%d\n", isNew?"new ":"", testClass.getSimpleName(), threadNo);
			}
			
			// check if stop happen 
			if( stopCycle ){
				return EXIT;
			}
		}
		return CONTINUE;
	}
	
	
	private Runnable[] collectAllTestMethods(Class<?> testClass) {
		
		List<Method> methods = new ArrayList<Method>(12);
		
		for (Method method : testClass.getDeclaredMethods()) {
			if (method.isAnnotationPresent(Test.class)) {
				methods.add(method);
			}
		}
		
		Runnable[] jobs = new Runnable[methods.size()]; 
		
		for(int i=0,max=methods.size(); i<max; i++){
			final Method method = methods.get(i);
			
			jobs[i] = new Runnable(){
				@Override
				public void run() {
					try {
						method.invoke(testInstance);
					} catch (Throwable e) {
						e.printStackTrace();
						stopCycle = true;
					}
				}
			};
		}
		
		methods.clear();
		methods = null;
		
		return jobs;
	}
}
