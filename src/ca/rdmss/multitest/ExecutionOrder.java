package ca.rdmss.multitest;

public enum ExecutionOrder {
	Sequentially("sequentially"),
	Parallel("in parallel");
	
	final private String descr;

	private ExecutionOrder(String descr) {
		this.descr = descr;
	}

	@Override
	public String toString() {
		return descr;
	}
}
