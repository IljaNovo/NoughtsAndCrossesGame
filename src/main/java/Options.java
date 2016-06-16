
public class Options {
	private int countConnections;
	private int activity;
	private boolean isWinOpponent;
	private boolean endGame;
	
	public Options() {
		this.activity = 1;
		this.countConnections = 0;
		this.isWinOpponent = false;
		this.endGame = false;
	}
	
	public void setEndGame(boolean end) {
		this.endGame = end;
	}

	public boolean getEndGame() {
		return this.endGame;
	}
	
	public void setStateWinOfOpponent(boolean isWin) {
		this.isWinOpponent = isWin;
	}
	
	public boolean getStateWinOfOpponent() {
		return this.isWinOpponent;
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
