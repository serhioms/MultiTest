package ca.mss.test.multitest.increment;

import org.junit.Test;

import ca.mss.multitest.ConcurrentTest;
import ca.mss.multitest.MultiTest;
import ca.mss.multitest.NewThread;

@ConcurrentTest( maxTry = IncrementSuite.MAX_TRY )
public class Increment_MultiThread_NonSynchronized extends Increment {

	@NewThread
	public void oneThread(){
		super.nonSynchronizedIncrement();
	}
	
	@NewThread
	public void secondThread(){
		super.nonSynchronizedIncrement();
	}
	
	@Test
	public void test() throws InstantiationException, IllegalAccessException, InterruptedException {
		System.out.println( MultiTest.start(this) + super.primitiveInt);
	}

}
