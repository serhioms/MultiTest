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
	final public static int MAX_THREAD = 2;
	
	/*
	 * Uncomment THREAD_SET array for getting table report from 1 to 32 threads (really long time) 
	 */
	final public static int[] THREAD_SET = null; // new int[]{1, 2, 3, 4, 8, 9, 10, 11, 12, 16, 32};

}
/* 2 Threads i7-3630QM 2.4Ghz (4 core)
===                          Increment_NonSynchronized done 2,000,000 time(s) in 329.0 mls (164.5  ns/try) ===  Totally incremented = 3,988,017
===                 Increment_NonSynchronized_Volatile done 2,000,000 time(s) in 609.0 mls (304.5  ns/try) ===  Totally incremented = 3,830,729
===                             Increment_Synchronized done 2,000,000 time(s) in 610.0 mls (305.0  ns/try) ===  Totally incremented = 4,000,000
===                    Increment_Synchronized_Volatile done 2,000,000 time(s) in 579.0 mls (289.5  ns/try) ===  Totally incremented = 4,000,000
===                                   Increment_Atomic done 2,000,000 time(s) in 751.0 mls (375.5  ns/try) ===  Totally incremented = 4,000,000
===                                     Increment_Lock done 2,000,000 time(s) in   1.4 sec (703.0  ns/try) ===  Totally incremented = 4,000,000


=== Increment_NonSynchronized done 2,000,000 time(s) ===
Threads Total      OneTry     OneTry(ns)
------- ---------- ---------- ----------
1       94.0   mls 47.0    ns     47.000
2       313.0  mls 156.5   ns    156.500
3       547.0  mls 273.5   ns    273.500
4       781.0  mls 390.5   ns    390.500
8       1.8    sec 875.0   ns    875.000
9       12.1   sec 6.1    mks   6070.500
10      15.8   sec 7.9    mks   7890.000
11      17.2   sec 8.6    mks   8610.000
12      18.2   sec 9.1    mks   9094.500
16      10.5   sec 5.3    mks   5250.000
32      26.0   sec 13.0   mks  12977.000
------- ---------- ---------- ----------
Totally incremented = 181,071,914

=== Increment_NonSynchronized_Volatile done 2,000,000 time(s) ===
Threads Total      OneTry     OneTry(ns)
------- ---------- ---------- ----------
1       95.0   mls 47.5    ns     47.500
2       484.0  mls 242.0   ns    242.000
3       812.0  mls 406.0   ns    406.000
4       820.0  mls 410.0   ns    410.000
8       1.8    sec 890.5   ns    890.500
9       12.0   sec 6.0    mks   5985.000
10      16.1   sec 8.1    mks   8052.500
11      17.8   sec 8.9    mks   8877.500
12      18.2   sec 9.1    mks   9093.500
16      11.2   sec 5.6    mks   5586.500
32      26.1   sec 13.0   mks  13047.500
------- ---------- ---------- ----------
Totally incremented = 179,510,296

=== Increment_Synchronized done 2,000,000 time(s) ===
Threads Total      OneTry     OneTry(ns)
------- ---------- ---------- ----------
1       79.0   mls 39.5    ns     39.500
2       531.0  mls 265.5   ns    265.500
3       782.0  mls 391.0   ns    391.000
4       1.0    sec 523.5   ns    523.500
8       6.5    sec 3.2    mks   3225.500
9       13.3   sec 6.7    mks   6655.500
10      16.5   sec 8.2    mks   8244.500
11      17.6   sec 8.8    mks   8820.500
12      18.4   sec 9.2    mks   9219.500
16      11.7   sec 5.8    mks   5836.000
32      27.8   sec 13.9   mks  13914.000
------- ---------- ---------- ----------
Totally incremented = 216,000,000

=== Increment_Synchronized_Volatile done 2,000,000 time(s) ===
Threads Total      OneTry     OneTry(ns)
------- ---------- ---------- ----------
1       79.0   mls 39.5    ns     39.500
2       610.0  mls 305.0   ns    305.000
3       922.0  mls 461.0   ns    461.000
4       1.1    sec 539.0   ns    539.000
8       5.5    sec 2.7    mks   2734.500
9       14.0   sec 7.0    mks   6982.500
10      16.6   sec 8.3    mks   8289.500
11      17.7   sec 8.8    mks   8849.000
12      18.2   sec 9.1    mks   9102.000
16      13.1   sec 6.5    mks   6543.000
32      29.2   sec 14.6   mks  14612.500
------- ---------- ---------- ----------
Totally incremented = 216,000,000

=== Increment_Atomic done 2,000,000 time(s) ===
Threads Total      OneTry     OneTry(ns)
------- ---------- ---------- ----------
1       79.0   mls 39.5    ns     39.500
2       344.0  mls 172.0   ns    172.000
3       750.0  mls 375.0   ns    375.000
4       797.0  mls 398.5   ns    398.500
8       1.6    sec 797.0   ns    797.000
9       11.9   sec 6.0    mks   5953.000
10      16.4   sec 8.2    mks   8189.000
11      17.4   sec 8.7    mks   8711.500
12      18.1   sec 9.0    mks   9039.500
16      11.0   sec 5.5    mks   5484.500
32      26.3   sec 13.2   mks  13164.500
------- ---------- ---------- ----------
Totally incremented = 216,000,000

=== Increment_Lock done 2,000,000 time(s) ===
Threads Total      OneTry     OneTry(ns)
------- ---------- ---------- ----------
1       110.0  mls 55.0    ns     55.000
2       1.1    sec 554.5   ns    554.500
3       1.9    sec 961.0   ns    961.000
4       2.7    sec 1.3    mks   1344.000
8       15.9   sec 8.0    mks   7968.000
9       17.1   sec 8.5    mks   8538.500
10      17.6   sec 8.8    mks   8817.500
11      19.5   sec 9.7    mks   9742.500
12      19.1   sec 9.6    mks   9551.000
16      19.9   sec 10.0   mks   9956.500
32      46.5   sec 23.2   mks  23245.500
------- ---------- ---------- ----------
Totally incremented = 216,000,000

*/