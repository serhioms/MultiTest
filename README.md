# MultiTest

If you really want to stress you code in multithreaded environment...

If you need to run couple threads exactly same time...

If you wish to repeat it 1 000 000 times or more...

Than that is exactly tool which you are looking for!


In couple steps:

1. Add multi-test.jar to your pom
2. Create junit4 test case
3. Add class annotation @ConcurrentTest( maxTry = 1_000_000 )
4. Create couple methods which represent your threads annotated @NewThread
5. Run your test @Test with MultiTest.start(this)

Here is example code and result:

=== MultiTestExample done 1,000,000 time(s) in 500.0 mls (500.0 ns/try) ===  Failed: count = 1,977,874 !


	@ConcurrentTest( maxTry = 1_000_000 )
	public class MultiTestExample {
	
		int count;

		@NewThread
		public void oneThread(){
			count++;
		}

		@NewThread
		public void secondThread(){
			count++;
		}

		@Test
		public void test() throws Throwable {

			String result = MultiTest.start(this);

			if( count != 2_000_000){
				System.out.printf("%s Failed: count = %,d !", result, count);
				fail("Do you really think int increment is thread safe!?");
			} else {
				System.out.printf("%s Ok: count = %,d !", result, count);
			}
		}
	}


PS Everything about increment in java see here in IncrementSuite.java
