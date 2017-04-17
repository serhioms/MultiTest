

import static org.junit.Assert.fail;

import org.junit.Test;

import ca.mss.impl.multitest.ConcurrentTest;
import ca.mss.impl.multitest.MultiTest;
import ca.mss.impl.multitest.NewThread;

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
			fail("Do you really think int increment is trade safe!?");
		} else {
			System.out.printf("%s Ok: count = %,d !", result, count);
		}
	}
}
