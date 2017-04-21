import org.junit.Rule;
import org.junit.Test;

import ca.rdmss.multitest.MultiTest;
import ca.rdmss.multitest.MultiTestRule;
import ca.rdmss.multitest.MultiThread;

@MultiTest(threadSet="2")
public class MultiTestDemo {
	@Rule
	public MultiTestRule multiTestRule = new MultiTestRule(MultiTestRule.DEMO);

	@Test
	@MultiThread(repeatNo=2)
	public void thread1(){
		System.out.print("thread1 ");
	}

	@Test
	@MultiThread(repeatNo=5, threadSet="5")
	public void thread2(){
		System.out.print("thread2 ");
	}
}
