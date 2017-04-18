package ca.mss.test.multitest.increment;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Increment {

	int primitiveInt;

	volatile int volatilePrimiriveInt;
	
	AtomicInteger atomicInteger = new AtomicInteger();
	
	ReentrantLock lock = new ReentrantLock();
	
	public void nonSynchronizedIncrement() {
		primitiveInt++;
	}

	synchronized public void synchronizedIncrement() {
		primitiveInt++;
	}

	public void volatileNonSynchronizedIncrement() {
		volatilePrimiriveInt++;
	}

	synchronized public void volatileSynchronizedIncrement() {
		volatilePrimiriveInt++;
	}

	public void atomicIncrement() {
		atomicInteger.incrementAndGet();
	}

	public void lockIncrement() {
		try {
			lock.lock();
			primitiveInt++;
		} finally {
			lock.unlock();
		}
	}


}
