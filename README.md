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
In this [example](https://github.com/serhioms/MultiTest/blob/master/test/ca/rdmss/test/multitest/test/MultiTestExample.java) count++ invokes 2 mln times simultaniously in 2 threads. 

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
Here is the output:

```text
=== MultiTestExampleRule done 1,000,000 time(s) in 266.0 mls (266.0  ns/try) ===  Failed: count = 1,932,125 !
```


PS Everything about increment in java you can find here [IncrementSuite.java](https://github.com/serhioms/MultiTest/blob/master/test/ca/rdmss/test/multitest/increment/IncrementSuite.java) or in details  [IncrementSuiteTable.java](https://github.com/serhioms/MultiTest/blob/master/test/ca/rdmss/test/multitest/increment/IncrementSuiteTable.java)
