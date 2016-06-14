public class Printer {
	public static void printInCosole(Symbols[][] field) {
		for (int row = 0; row < field.length; ++row) {
			for (int column = 0; column < field.length; ++column) {
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
}
