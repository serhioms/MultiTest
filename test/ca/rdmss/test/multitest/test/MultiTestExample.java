package ca.rdmss.test.multitest.test;

import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;

import ca.rdmss.multitest.annotation.MultiTest;
import ca.rdmss.multitest.annotation.MultiThread;
import ca.rdmss.multitest.junitrule.MultiTestRule;

@MultiTest(repeatNo = 1_000_000, threadSet="2")
public class MultiTestExample {

	@Rule
	public MultiTestRule rule = new MultiTestRule(this);

	int count;
	
	@MultiThread
	public void thread(){
		count++;
	}
	
	@Test
	public void test(){
		if( count != 2_000_000){
			System.out.printf("%s Failed: count = %,d !", rule.getReport(), count);
			fail("Do you really think int increment is trade safe!?");
		} else {
			System.out.printf("%s Ok: count = %,d !", rule.getReport(), count);
		}
	}
	
}
/* i7-3630QM 2.4Ghz (4 core)


=== MultiTestExampleRule done 1,000,000 time(s) in 266.0 mls (266.0  ns/try) ===  Failed: count = 1,932,125 !
*/