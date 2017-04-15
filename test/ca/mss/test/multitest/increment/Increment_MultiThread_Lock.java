package ca.mss.test.multitest.increment;

import org.junit.Test;

import ca.mss.impl.multitest.ConcurrentTest;
import ca.mss.impl.multitest.MultiTest;
import ca.mss.impl.multitest.NewThread;

@ConcurrentTest( maxTry = IncrementSuite.MAX_TRY )
public class Increment_MultiThread_Lock extends Increment {

	@NewThread
	public void oneThread(){
		super.lockIncrement();
	}
	
	@NewThread
	public void secondThread(){
		super.lockIncrement();
	}
	
	@Test
	public void test() throws InstantiationException, IllegalAccessException, InterruptedException {
		System.out.println( MultiTest.start(this) + super.primitiveInt);
	}

}
