package ca.rdmss.test.multitest.test;

import org.junit.Rule;
import org.junit.Test;

import ca.rdmss.multitest.annotation.MultiTest;
import ca.rdmss.multitest.annotation.MultiThread;
import ca.rdmss.multitest.junitrule.MultiTestRule;

@MultiTest(repeatNo = 1_000_000, threadSet="1,2,3,4,5,6,7,8,9,10,12,16,32")
public class MultiTestExampleTable {

	@Rule
	public MultiTestRule rule = new MultiTestRule(this);

	int count;
	
	@MultiThread
	public void thread(){
		count++;
	}
	
	@Test
	public void test(){
		System.out.println(rule.getReport());
		System.out.printf("Totally incremented = %,d\n", count);
	}
	
}
/* i7-3630QM 2.4Ghz (4 core) 

=== MultiTestExampleTable done 1,000,000 time(s) ===
Threads Total      OneTry     OneTry(ns)
------- ---------- ---------- ----------
1       46.0   mls 46.0    ns     46.000
2       178.0  mls 178.0   ns    178.000
3       267.0  mls 267.0   ns    267.000
4       370.0  mls 370.0   ns    370.000
5       424.0  mls 424.0   ns    424.000
6       510.0  mls 510.0   ns    510.000
7       627.0  mls 627.0   ns    627.000
8       772.0  mls 772.0   ns    772.000
9       9.5    sec 9.5    mks   9497.000
10      11.9   sec 11.9   mks  11855.000
12      17.3   sec 17.3   mks  17302.000
16      6.6    sec 6.6    mks   6630.000
32      17.6   sec 17.6   mks  17630.000
------- ---------- ---------- ----------
Totally incremented = 88,783,987
*/