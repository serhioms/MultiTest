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
public class IncrementSuiteTable {
	
	final public static int MAX_TRY = 2000000; 
	final public static String THREAD_SET = "1,2,3,4,5,6,7,8,9,10,11,12,16,32";

}
/*															i7-3630QM 2.4Ghz (4 core)
=== Increment_NonSynchronized done 2,000,000 time(s) ===
Threads Total      OneTry     OneTry(ns)
------- ---------- ---------- ----------
1       94.0   mls 47.0    ns     47.000
2       390.0  mls 195.0   ns    195.000
3       626.0  mls 313.0   ns    313.000
4       752.0  mls 376.0   ns    376.000
5       890.0  mls 445.0   ns    445.000
6       1.1    sec 539.5   ns    539.500
7       1.3    sec 625.0   ns    625.000
8       1.5    sec 726.5   ns    726.500
9       12.5   sec 6.2    mks   6243.500
10      16.0   sec 8.0    mks   8023.500
11      17.3   sec 8.7    mks   8657.000
12      18.4   sec 9.2    mks   9197.000
16      10.3   sec 5.1    mks   5125.500
32      25.7   sec 12.8   mks  12846.000
------- ---------- ---------- ----------
Totally incremented = 198,318,017

=== Increment_NonSynchronized_Volatile done 2,000,000 time(s) ===
Threads Total      OneTry     OneTry(ns)
------- ---------- ---------- ----------
1       111.0  mls 55.5    ns     55.500
2       390.0  mls 195.0   ns    195.000
3       875.0  mls 437.5   ns    437.500
4       844.0  mls 422.0   ns    422.000
5       1.0    sec 500.0   ns    500.000
6       1.3    sec 625.0   ns    625.000
7       1.5    sec 727.0   ns    727.000
8       1.7    sec 828.5   ns    828.500
9       12.3   sec 6.1    mks   6149.500
10      16.3   sec 8.1    mks   8133.500
11      17.4   sec 8.7    mks   8687.500
12      18.3   sec 9.1    mks   9143.000
16      10.5   sec 5.2    mks   5242.000
32      26.5   sec 13.2   mks  13235.000
------- ---------- ---------- ----------
Totally incremented = 200,250,886

=== Increment_Synchronized done 2,000,000 time(s) ===
Threads Total      OneTry     OneTry(ns)
------- ---------- ---------- ----------
1       94.0   mls 47.0    ns     47.000
2       469.0  mls 234.5   ns    234.500
3       1.0    sec 515.5   ns    515.500
4       1.0    sec 523.5   ns    523.500
5       1.3    sec 664.0   ns    664.000
6       2.8    sec 1.4    mks   1407.000
7       2.8    sec 1.4    mks   1414.500
8       4.3    sec 2.2    mks   2165.000
9       13.5   sec 6.7    mks   6726.500
10      16.7   sec 8.3    mks   8344.500
11      17.6   sec 8.8    mks   8782.500
12      18.5   sec 9.3    mks   9253.500
16      11.3   sec 5.6    mks   5641.000
32      27.8   sec 13.9   mks  13915.500
------- ---------- ---------- ----------
Totally incremented = 252,000,000

=== Increment_Synchronized_Volatile done 2,000,000 time(s) ===
Threads Total      OneTry     OneTry(ns)
------- ---------- ---------- ----------
1       111.0  mls 55.5    ns     55.500
2       609.0  mls 304.5   ns    304.500
3       969.0  mls 484.5   ns    484.500
4       1.1    sec 562.5   ns    562.500
5       1.6    sec 820.5   ns    820.500
6       4.1    sec 2.1    mks   2063.000
7       4.0    sec 2.0    mks   1992.000
8       5.4    sec 2.7    mks   2680.000
9       13.9   sec 6.9    mks   6930.000
10      16.8   sec 8.4    mks   8391.500
11      17.6   sec 8.8    mks   8782.500
12      18.2   sec 9.1    mks   9102.000
16      12.6   sec 6.3    mks   6313.500
32      29.1   sec 14.5   mks  14526.000
------- ---------- ---------- ----------
Totally incremented = 252,000,000

=== Increment_Atomic done 2,000,000 time(s) ===
Threads Total      OneTry     OneTry(ns)
------- ---------- ---------- ----------
1       95.0   mls 47.5    ns     47.500
2       406.0  mls 203.0   ns    203.000
3       797.0  mls 398.5   ns    398.500
4       797.0  mls 398.5   ns    398.500
5       984.0  mls 492.0   ns    492.000
6       1.2    sec 594.0   ns    594.000
7       1.4    sec 695.5   ns    695.500
8       1.6    sec 796.5   ns    796.500
9       12.4   sec 6.2    mks   6220.000
10      16.2   sec 8.1    mks   8078.500
11      17.5   sec 8.7    mks   8734.500
12      18.3   sec 9.1    mks   9142.000
16      10.5   sec 5.2    mks   5227.000
32      26.1   sec 13.0   mks  13032.500
------- ---------- ---------- ----------
Totally incremented = 252,000,000

=== Increment_Lock done 2,000,000 time(s) ===
Threads Total      OneTry     OneTry(ns)
------- ---------- ---------- ----------
1       126.0  mls 63.0    ns     63.000
2       859.0  mls 429.5   ns    429.500
3       3.9    sec 1.9    mks   1946.000
4       3.8    sec 1.9    mks   1914.000
5       6.1    sec 3.1    mks   3063.000
6       9.3    sec 4.7    mks   4672.500
7       12.4   sec 6.2    mks   6180.000
8       16.4   sec 8.2    mks   8211.500
9       17.0   sec 8.5    mks   8487.000
10      17.6   sec 8.8    mks   8820.500
11      19.3   sec 9.6    mks   9649.500
12      19.0   sec 9.5    mks   9475.500
16      19.6   sec 9.8    mks   9821.000
32      46.1   sec 23.1   mks  23057.000
------- ---------- ---------- ----------
Totally incremented = 252,000,000
*/