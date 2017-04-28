package ca.rdmss.test.multitest.increment;

import org.junit.Rule;
import org.junit.Test;

import ca.rdmss.multitest.annotation.MultiTest;
import ca.rdmss.multitest.annotation.MultiThread;
import ca.rdmss.multitest.junitrule.MultiTestRule;

@MultiTest(repeatNo=IncrementSuite.MAX_TRY, threadSet=IncrementSuite.THREAD_SET)
public class Increment_Synchronized {

	@Rule
	public MultiTestRule rule = new MultiTestRule(this);

	int counter;

	@MultiThread
	public void thread(){
		synchronized( this ){
			counter++;
		}
	}
	
	@Test
	public void test() throws InstantiationException, IllegalAccessException, InterruptedException {
		System.out.printf("%s%sTotally incremented = %,d\n", rule.getResult(), (rule.isTable()? "\n":" "), counter);
	}
}
