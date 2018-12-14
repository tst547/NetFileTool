package cn.hy.netfiletool.net.download;


public class Progress {

	private volatile int current;
	private volatile long offset;
	private long max;

	public Progress() {
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public long getMax() {
		return max;
	}

	public void setMax(long max) {
		this.max = max;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		if (current!=this.current)
			this.current = current;
	}
}
