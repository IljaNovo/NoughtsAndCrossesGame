
public class Options {
	private int countConnections;
	private int activity;
	
	public Options() {
		this.activity = 1;
		this.countConnections = 0;
	}
	
	public int getCountConnections() {
		return this.countConnections;
	}
	
	public int getActivity() {
		return this.activity;
	}
	
	public void increaseCountConnections(int count) {
		this.countConnections += count;
	}
	
	public void reduceCountConnections(int count) {
		this.countConnections -= count;
	}
	
	public void changeActivity() {
		if (this.activity == 1) {
			this.activity = 2;
		} else {
			this.activity = 1;
		}	
	}
}
