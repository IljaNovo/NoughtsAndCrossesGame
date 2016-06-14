public class Game {
	private Symbols[][] field;
	
	public Game(int n) {
		this.field = new Symbols[n][n];
		this.clearfield();
	}
	
	public void clearfield() {
		for (int i = 0; i < field.length; ++i) {
			for (int j = 0; j < field.length; ++j) {
				this.field[i][j] = Symbols.empty;
			}
		}
	}
	
	public void printStatus() {
		for (int row = 0; row < this.field.length; ++row) {
			for (int column = 0; column < this.field.length; ++column) {
				if (field[row][column] == Symbols.cross) {
					System.out.print("X ");
				}
				if (field[row][column] == Symbols.nought) {
					System.out.print("0 ");
				}
				if (field[row][column] == Symbols.empty) {
					System.out.print("* ");
				}
			}
			System.out.println();
		}
	}
	
	public void setCross(int x, int y) {
		this.field[x][y] = Symbols.cross;
	}
	
	public void setNoght(int x, int y) {
		this.field[x][y] = Symbols.nought;
	}
	
	public boolean checkWin(Symbols s) {
		return checkVerticals(s) ||
			   checkHorizontals(s) ||
			   checkDiagonals(s);
	}
	
	private boolean checkVerticals(Symbols s) {
		boolean isWin = true;
		for (int row = 0; row < this.field.length; ++row) {
			for (int column = 0; column < this.field.length; ++column) {
				isWin = isWin && (field[row][column] == s);
			}
			if (isWin) {
				return true;
			} else {
				isWin = true;
			}
		}
		return false;
	}
	
	private boolean checkHorizontals(Symbols s) {
		boolean isWin = true;
		for (int column = 0; column < this.field.length; ++column) {
			for (int row = 0; row < this.field.length; ++row) {
				isWin = isWin && (field[column][row] == s);
			}
			if (isWin) {
				return true;
			} else {
				isWin = true;
			}
		}
		return false;
	}
	
	private boolean checkDiagonals(Symbols s) {
		return (checkHeadDiagonal(s) == true) ||
				(checkSideDiagonal(s) == true);
	}
	
	private boolean checkHeadDiagonal(Symbols s) {
		boolean isWin = true;
		for (int i = 0; i < this.field.length; ++i) {
			isWin = isWin && (field[i][i] == s);
		}
		if (isWin) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean checkSideDiagonal(Symbols s) {
		boolean isWin = true;
		int row = this.field.length;
		int column = 0;
		for (int i = 0; i < this.field.length; ++i) {
			isWin = isWin && (field[row - i][column + i] == s);
		}
		if (isWin) {
			return true;
		} else {
			return false;
		}
	}
}
