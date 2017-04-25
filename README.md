# MultiTest
JUnit 4 rule and annotations to run in parallel your code in multiple threads: @MultiTest, @MultiThread, @MultyCycle

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
In this [example](https://github.com/serhioms/MultiTest/blob/master/test/ca/rdmss/test/multitest/test/MultiTestExample.java) count++ invokes 1 mln times simultaniously in 2 threads. 

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
count++ invokes 1 mln times simultaniously in 1,2,3,4,5,6,7,8,9,10,12,16,32 threads consequently. 

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


PS Everything about increment in java you can find here [IncrementSuite.java](https://github.com/serhioms/MultiTest/blob/master/test/ca/rdmss/test/multitest/increment/IncrementSuite.java) or in details  [IncrementSuiteTable.java](https://github.com/serhioms/MultiTest/blob/master/test/ca/rdmss/test/multitest/increment/IncrementSuiteTable.java)
