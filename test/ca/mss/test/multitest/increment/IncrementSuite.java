package ca.mss.test.multitest.increment;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	Increment_SingleThread_NonSynchronized.class,
	Increment_SingleThread_NonSynchronized_Volatile.class,
	Increment_SingleThread_Synchronized.class,
	Increment_SingleThread_Synchronized_Volatile.class,
	Increment_SingleThread_Atomic.class,
	Increment_SingleThread_Lock.class,
	
	Increment_MultiThread_NonSynchronized.class,
	Increment_MultiThread_NonSynchronized_Volatile.class,
	Increment_MultiThread_Synchronized.class,
	Increment_MultiThread_Synchronized_Volatile.class,
	Increment_MultiThread_Atomic.class,
	Increment_MultiThread_Lock.class

	})
public class IncrementSuite {
	
	final public static int MAX_TRY = 2000000; 

}
