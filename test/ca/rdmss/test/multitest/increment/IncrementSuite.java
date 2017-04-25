package ca.rdmss.test.multitest.increment;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	Increment_NonSynchronized.class,
	Increment_NonSynchronized_Volatile.class,
	Increment_Synchronized.class,
	Increment_Synchronized_Volatile.class,
	Increment_Atomic.class,
	Increment_Lock.class
	})
public class IncrementSuite {
	
	final public static int MAX_TRY = 2000000; 
	final public static String THREAD_SET = "2";

}
/* i7-3630QM 2.4Ghz (4 core)    2 Threads only
===                          Increment_NonSynchronized done 2,000,000 time(s) in 344.0 mls (172.0  ns/try) === Totally incremented = 3,978,226
===                 Increment_NonSynchronized_Volatile done 2,000,000 time(s) in 593.0 mls (296.5  ns/try) === Totally incremented = 3,928,733
===                             Increment_Synchronized done 2,000,000 time(s) in 564.0 mls (282.0  ns/try) === Totally incremented = 4,000,000
===                    Increment_Synchronized_Volatile done 2,000,000 time(s) in 531.0 mls (265.5  ns/try) === Totally incremented = 4,000,000
===                                   Increment_Atomic done 2,000,000 time(s) in 470.0 mls (235.0  ns/try) === Totally incremented = 4,000,000
===                                     Increment_Lock done 2,000,000 time(s) in   1.6 sec (812.5  ns/try) === Totally incremented = 4,000,000
*/