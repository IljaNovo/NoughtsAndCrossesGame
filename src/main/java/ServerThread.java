
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerThread extends Thread {
	private List<Socket> connections;
	private volatile Game game;
	private volatile Options options;
	private volatile Symbols symbol;
	private volatile int playerID;
	
	public ServerThread(List<Socket> connections, Game game, Options options) {
		this.connections = connections;
		this.game = game;
		this.options = options;
		this.playerID = this.options.getCountConnections();
		if (this.options.getCountConnections() == 1) {
			this.symbol = Symbols.cross;
		} else {
			this.symbol = Symbols.nought;
		}
	}
	
	private void waitOpponent(BufferedReader input, PrintWriter output) {
		output.println("Move opponent...\n");
		output.flush();
	}
	
	private boolean checkCoordsStr(String coordsStr) {
		Pattern p = Pattern.compile("^\\d\\,\\d$");
		Matcher m = p.matcher(coordsStr);
		return m.matches();
	}
	
	private void makeMove(String coordsStr, BufferedReader input, PrintWriter output) throws IOException {
//		while(true) {
			if (!checkCoordsStr(coordsStr)) {
				output.println("Coords is not valid...\n0");
				output.flush();
				return;
			}
			int[] coords = Parser.parseCoords(coordsStr);
			if (!game.setSymbol(coords[0], coords[1], this.symbol)) {
				output.println("Call is not empty...\n0");
				output.flush();
//				continue;
			} else {
				game.setSymbol(coords[0], coords[1], this.symbol);
				this.options.changeActivity();
				this.waitOpponent(input, output);
				this.sendMessageOponent("Your move\n" + Printer.print(game.getField()) + "\n0");
			}
//			break;
//		}
	}
	
	private void sendMessageOponent(String message) throws IOException {
		PrintWriter output = null;
		if (this.playerID - 1  == 0) {
			output = new PrintWriter(
				connections.get(1).getOutputStream());
		} else {
			output = new PrintWriter(
			connections.get(0).getOutputStream());
		}
		output.println(message);
		output.flush();
	}
	
	private void makeAction(String coordStr, BufferedReader input, PrintWriter output) throws IOException {
//		if (this.options.getEndGame() == true) {
//			return;
//		}
		if (this.options.getActivity() == this.playerID) {
			this.makeMove(coordStr, input, output);
//			this.checkWin();
//			if (this.options.getStateWinOfOpponent() == true) {
//				output.println("You win!\n0");
//				this.options.setEndGame(true);
//				output.flush();
//			}
		} else {
			this.waitOpponent(input, output);
		}
	}
	
	private void checkWin() {
		if (this.game.checkWin(this.symbol)) {
			this.options.setStateWinOfOpponent(true);
		}
	}
	
	private void readMessageOfClient(BufferedReader input, PrintWriter output) throws IOException {
		String message = null;
		while ((message = input.readLine()) != null) {
			if (options.getCountConnections() == 2) {
				this.makeAction(message, input, output);
//				this.sendInitialNotification(input, output);
//				if (this.options.getStateWinOfOpponent() == true) {
//					output.println("You lose...\n0");
//					output.flush();
//					break;
//				}
			} else {
				output.println("Waiting for player...\n0");
				output.flush();
			}
		}
	}
	
	@Override
	public void run() {
		try {
			PrintWriter output = new PrintWriter(
					connections.get(this.playerID - 1).getOutputStream(),
					true);
			BufferedReader input = new BufferedReader(
					new InputStreamReader(
							connections.get(this.playerID - 1).getInputStream()));
			System.out.println("user " + input.readLine() + " is now connected to the server...");
			this.readMessageOfClient(input, output);
			
			Thread.currentThread().sleep(5000);
			connections.get(this.playerID - 1).close();
		} catch (IOException e) {
			
		} catch (InterruptedException e) {
			
		}
	}
}
