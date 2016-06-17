
public class Printer {
	public static String print(Symbols[][] field) {
		String answer = "";
		
		for (int row = 0; row < field.length; ++row) {
			for (int column = 0; column < field.length; ++column) {
				if (field[row][column] == Symbols.cross) {
					answer += "X ";
				}
				if (field[row][column] == Symbols.nought) {
					answer += "0 ";
				}
				if (field[row][column] == Symbols.empty) {
					answer += "* ";
				}
			}
			answer += "\n";
		}
		return answer;
	}
}
