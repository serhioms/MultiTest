package ca.rdmss.test.multitest.synchronization.impl;

public class Singlton<T> {

	volatile T box;

	public T getNoSynchronization() {
		return box;
	}

	public void setNoSynchronization(T t) {
		if( box == null ){
			box = t;
		}
	}

}
