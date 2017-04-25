package ca.rdmss.test.multitest.synchronization;

import org.junit.Rule;
import org.junit.Test;

import ca.rdmss.multitest.MultiCycle;
import ca.rdmss.multitest.MultiTest;
import ca.rdmss.multitest.MultiTestRule;
import ca.rdmss.multitest.MultiThread;
import ca.rdmss.test.multitest.synchronization.impl.Singlton;
import ca.rdmss.test.util.TestUtil;

@MultiTest(repeatNo=1_000, newInstance=true)
public class Singlton_NoSynchronization {

	@Rule
	public MultiTestRule rule = new MultiTestRule();

	Singlton<Integer> test = new Singlton<Integer>();
	Integer a, b, c;

	@MultiThread
	public void thread1(){
		test.setNoSynchronization(1);
		a = test.getNoSynchronization();
	}
	
	@MultiThread
	public void thread2(){
		test.setNoSynchronization(2);
		b = test.getNoSynchronization();
	}
	
	@MultiThread
	public void thread3(){
		c = test.getNoSynchronization();
	}

	static TestUtil util = new TestUtil();
	
	@MultiCycle
	public void cycle(){
		util.count(a+"_"+b+"_"+c);
	}
	
	@Test
	public void result(){
		util.print();
	}
}
/*
*/