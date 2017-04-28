# MultiTest
JUnit 4 rule and annotations to run in parallel your code in multiple threads: @MultiTest, @MultiThread, @MultyEndOfCycle, @MultyEndOfSet, @MultyBefore

Based on java.util.concurrent.Phaser

## Usage

### Declaring dependency
#### Maven

```xml
    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

### Using in test
In this [example](https://github.com/serhioms/MultiTest/blob/master/test/ca/rdmss/test/multitest/test/MultiTestExample.java) count++ invokes 1 mln times simultaneously in 2 threads. Each cycle of start of 2 threads use the same instance of `MultiTestExample` - `this`. You have to put it in `MultiTestRule` constructor otherwise instance will be created automatically but you never get access to it.

```java
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
			System.out.printf("%s Failed: count = %,d !", rule.getResult(), count);
			fail("Do you really think int increment is trade safe!?");
		} else {
			System.out.printf("%s Ok: count = %,d !", rule.getResult(), count);
		}
	}
}	
```
Here is output for i7-3630QM 2.4Ghz (4 core):

```text
=== MultiTestExampleRule done 1,000,000 time(s) in 266.0 mls (266.0  ns/try) ===  Failed: count = 1,932,125 !
```



### Here is more detailed [example](https://github.com/serhioms/MultiTest/blob/master/test/ca/rdmss/test/multitest/test/MultiTestExampleTable.java)
count++ invokes 1 mln times simultaneously in 1,2,3,4,5,6,7,8,9,10,12,16,32 thread sets consequently. Each set runs against the same `MultiTestExampleTable` object - `this`.

```java
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
		System.out.printf("%s\nTotally incremented = %,d\n", rule.getResult(), count);
	}
	
}	
```
Here is output for i7-3630QM 2.4Ghz (4 core):

```text
=== MultiTestExampleTable done 1,000,000 time(s) ===
Threads Total      OneTry     OneTry(ns)
------- ---------- ---------- ----------
1       63.0   mls 63.0    ns     63.000
2       187.0  mls 187.0   ns    187.000
3       282.0  mls 282.0   ns    282.000
4       359.0  mls 359.0   ns    359.000
5       500.0  mls 500.0   ns    500.000
6       533.0  mls 533.0   ns    533.000
7       641.0  mls 641.0   ns    641.000
8       750.0  mls 750.0   ns    750.000
9       6.2    sec 6.2    mks   6170.000
10      8.0    sec 8.0    mks   8034.000
12      9.1    sec 9.1    mks   9056.000
16      5.7    sec 5.7    mks   5708.000
32      13.4   sec 13.4   mks  13422.000
------- ---------- ---------- ----------
Totally incremented = 90,249,773
```

It is clear now that perfomance of you code getting lower and lower in multithreded environment! For 1 thread single increment cost 63 nano seconds. For 2 concurrent threads the cost get tripled - 187 ns, for 3 threads it is 500% more - 282 ns... But it is still more over lineral up to 8 threads. For 9 threads and more the cost grows exponentially!!!

I get it through why Gurus do not advise have more threads in your application then cores per socket of your's CPU.

I think about tool which runs above test to calculate maximum threads for proper initialization of executor pool...

PS Everything about increment in java you can find here [IncrementSuite.java](https://github.com/serhioms/MultiTest/blob/master/test/ca/rdmss/test/multitest/increment/IncrementSuite.java) or in details - [IncrementSuiteTable.java](https://github.com/serhioms/MultiTest/blob/master/test/ca/rdmss/test/multitest/increment/IncrementSuiteTable.java)



### Here is more complicated [example](https://github.com/serhioms/MultiTest/blob/master/test/ca/rdmss/test/multitest/test/MultiTestCycleExample.java)
There are 3 methods defined here: `thread1()`, `thread2()` and `thread3()`. All of them run simultaneously 1 mln time in 3 separate threads. Annotation `@MultiTest` contains `true` for new instance... It means before each of cycle of start 3 threads new `MultiTestCycleExample` instance created for them! More over after each cycle the actual `a,b,c` values will save into map in `endOfCycle()` method vie `@MultiEndOfCycle` annotation. Finally `Util.print()` shows map of keys, percentage and actual counters. 

```java
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
	public void endOfCycle(){
		util.count(a+"_"+b+"_"+c);
	}
	
	@Test
	public void result(){
		System.out.println(rule.getResult());
		util.print();
	}
}	
```
The output for i7-3630QM 2.4Ghz (4 core) below:

```text

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
```
It is quite clear now why simple code running in 3 parallel threads must be synchronized. Frankly speacking I've expected result like this `a,b,c={1,2,X}` where `X=0,1,2`. But how come `a,b,c={1,1,X}||{2,2,X}`? Any idea? May be `volatile` will help? :)


## Annotations

### @MultiTest - class level

Define 3 parameters:

repeatNo = 1 by default. How many cycles must be done for each set. 

threadSet = "1" by default. How many threads must be run in parallel in each cycle. You can define many sets as "1,2,4,8,16" - means run repeatNo times 1 thread, 2 threads, ..., 16 threads in parallel.

newInstance = false by default. Should new instance be created before each cycle. False means that all cycles for all threadSet runs against same instance. True means that before each cycle new instance of @MultiTest class be created.

### @MultiThread - method level

No parameters. Annotated method become a separate thread. Total amount of running threads in each cycle for the threadSet="3" is 3*(How many methods are annotaded by @MultiThread)

### @MultyEndOfCycle - method level

No parameters. Annotated method invokes at the end of each cycle. If repeatNo=100 then @MultyEndOfCycle method invokes 100 times for each threadSet.

### @MultyEndOfSet - method level

No parameters. Annotated method invokes at the end of each thread set. If threadSet="1,2,4,8,16" then @MultyEndOfSet method invokes 5 times.

### @MultyBefore - method level

No parameters. Annotated method invokes just once before whole test.

## MultiTest diagram

![alt text](https://github.com/serhioms/MultiTest/blob/master/result/MultiTest%20diagram.png)



