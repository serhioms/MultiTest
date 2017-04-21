package ca.rdmss.test.multitest.increment;

import org.junit.Test;

import ca.rdmss.multitest.ConcurrentTest;
import ca.rdmss.multitest.MultiTest;
import ca.rdmss.multitest.NewThread;

@ConcurrentTest( maxTry = IncrementSuite.MAX_TRY )
public class Increment_MultiThread_Lock extends Increment {

	@NewThread(howMany=2)
	public void thread(){
		lockIncrement();
	}
	
	@Test
	public void test() throws InstantiationException, IllegalAccessException, InterruptedException {
		System.out.println( MultiTest.start(this) + primitiveInt);
	}

}
