

import static org.junit.Assert.fail;

import org.junit.Test;

import ca.rdmss.multitest.ConcurrentTest;
import ca.rdmss.multitest.MultiTest;
import ca.rdmss.multitest.NewThread;

@ConcurrentTest( maxTry = 1_000_000 )
public class MultiTestExample {

	int count;
	
	@NewThread(howMany=3)
	public void thread(){
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
