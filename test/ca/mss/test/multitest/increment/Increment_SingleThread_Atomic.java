package ca.mss.test.multitest.increment;

import org.junit.Test;

import ca.mss.impl.multitest.ConcurrentTest;
import ca.mss.impl.multitest.MultiTest;
import ca.mss.impl.multitest.NewThread;

@ConcurrentTest( maxTry = IncrementSuite.MAX_TRY )
public class Increment_SingleThread_Atomic extends Increment {

	@NewThread
	public void oneThread(){
		super.atomicIncrement();
	}
	
	
	@Test
	public void test() throws InstantiationException, IllegalAccessException, InterruptedException {
		System.out.println( MultiTest.start(this) + super.atomicInteger.get());
	}

}
