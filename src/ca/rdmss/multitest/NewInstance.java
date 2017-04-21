package ca.rdmss.multitest;

public enum NewInstance {
	True("new"),
	False(""),
	NA("n/a");
	
	final public String descr;

	private NewInstance(String descr) {
		this.descr = descr;
	}

	@Override
	public String toString() {
		return descr;
	}
}
