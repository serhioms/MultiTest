package ca.rdmss.test.multitest.test;

import org.junit.Rule;
import org.junit.Test;

import ca.rdmss.multitest.MultiEndOfCycle;
import ca.rdmss.multitest.MultiTest;
import ca.rdmss.multitest.MultiTestRule;
import ca.rdmss.multitest.MultiThread;
import ca.rdmss.test.util.TestUtil;

@MultiTest(repeatNo=1_000_000, newInstance=true)
public class MultiTestCycleExample {

	@Rule
	public MultiTestRule rule = new MultiTestRule();

	int integer;
	int a, b, c;

	@MultiThread
	public void thread1(){
		integer = 1;
		a = integer;
	}
	
	@MultiThread
	public void thread2(){
		integer = 2;
		b = integer;
	}
	
	@MultiThread
	public void thread3(){
		c = integer;
	}

	static TestUtil util = new TestUtil();
	
	@MultiEndOfCycle
	public void cycle(){
		util.count(a+"_"+b+"_"+c);
	}
	
	@Test
	public void result(){
		System.out.println(rule.getResult());
		util.print();
	}
}
/* i7-3630QM 2.4Ghz (4 core)

===                              MultiTestCycleExample done 1,000,000 time(s) in   2.7 sec (  2.7 mks/try) ===

                                               Key Percent   Actual val
                                ------------------ ------- ------------
                                             1_2_1  34.2 %      342,169
                                             1_2_2  31.8 %      317,806
                                             1_1_0   0.0 %           32
                                             1_2_0  34.0 %      339,872
                                             1_1_1   0.0 %           21
                                             2_2_2   0.0 %           12
                                             2_2_0   0.0 %           88
                                ------------------ ------- ------------
*/