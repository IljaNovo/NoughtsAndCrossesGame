
public class Parser {
	public static int[] parseCoords(String coordsStr) {
		int[] coordsInt = new int[2];
		coordsInt[0] = Integer.parseInt(String.valueOf(coordsStr.charAt(0)));
		coordsInt[1] = Integer.parseInt(String.valueOf(coordsStr.charAt(3)));
		return coordsInt;
	}
}
