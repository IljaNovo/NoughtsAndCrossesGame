import static org.junit.Assert.*;

import org.junit.Test;

import junit.framework.Assert;

public class GameTest {

	@Test
	public void testCreateField() {
		Game game = new Game(3);
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				Assert.assertEquals(game.getField()[i][j], Symbols.empty);
			}
		}
	}
	
	@Test
	public void testSetSymbols() {
		Game game = new Game(3);
		game.setNoght(1, 1);
		game.setCross(2, 2);
		Assert.assertEquals(game.getField()[2][2], Symbols.cross);
		Assert.assertEquals(game.getField()[1][1], Symbols.nought);
	}
	
	@Test
	public void testClearField() {
		Game game = new Game(3);
		game.setNoght(1, 1);
		game.setCross(2, 2);
		game.clearField();
		
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				Assert.assertEquals(game.getField()[i][j], Symbols.empty);
			}
		}
	}
	
	@Test
	public void testIsWinHeadDiagonal() {
		Game game = new Game(3);
		game.setCross(0, 0);
		game.setCross(1, 1);
		game.setCross(2, 2);
		Assert.assertEquals(game.checkWin(Symbols.cross), true);
	}

	@Test
	public void testIsWinSideDiagonal() {
		Game game = new Game(3);
		game.setNoght(2, 0);
		game.setNoght(1, 1);
		game.setNoght(0, 2);
		int row = game.getField().length - 1;
		int column = 0;
		for (int i = 0; i < game.getField().length; ++i) {
			Assert.assertEquals(game.getField()[row - i][column + i], Symbols.nought);
		}
	}
	
	@Test
	public void testIsWinVertikal() {
		Game game = new Game(3);
		game.setNoght(0, 1);
		game.setNoght(2, 1);
		game.setNoght(1, 1);
		Assert.assertEquals(game.checkWin(Symbols.nought), true);
	}
	
	@Test
	public void testIsWinHorizontal() {
		Game game = new Game(3);
		game.setCross(2, 0);
		game.setCross(2, 1);
		game.setCross(2, 2);
		Assert.assertEquals(game.checkWin(Symbols.cross), true);
	}
	
	@Test
	public void testIsNotEmpty() {
		Game game = new Game(3);
		game.setCross(2, 2);
		Assert.assertFalse(game.setNoght(2, 2));
	}
}
