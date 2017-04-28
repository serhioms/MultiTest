package ca.rdmss.test.multitest.synchronization;

import org.junit.Rule;
import org.junit.Test;

import ca.rdmss.multitest.annotation.MultiEndOfCycle;
import ca.rdmss.multitest.annotation.MultiTest;
import ca.rdmss.multitest.annotation.MultiThread;
import ca.rdmss.multitest.junitrule.MultiTestRule;
import ca.rdmss.test.multitest.synchronization.impl.Singlton;
import ca.rdmss.util.UtilTest;

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

	static UtilTest util = new UtilTest();
	
	@MultiEndOfCycle
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