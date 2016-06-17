
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
	private volatile String name;

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

	private void waitOpponent() throws IOException {
		this.sendMessageActiveCurrentConsole("\nMove opponent...\n" + Printer.print(game.getField()));
	}

	private boolean checkCoordsStr(String coordsStr) {
		Pattern p = Pattern.compile("^\\d\\,\\d$");
		Matcher m = p.matcher(coordsStr);
		return m.matches();
	}

	private void makeMove(String coordsStr) throws IOException {
		if (!checkCoordsStr(coordsStr)) {
			this.sendMessageActiveCurrentConsole("Coords is not valid...\n0");
			return;
		}
		int[] coords = Parser.parseCoords(coordsStr);
		if (!game.setSymbol(coords[0], coords[1], this.symbol)) {
			this.sendMessageActiveCurrentConsole("Call is not empty...\n0");
		} else {
			game.setSymbol(coords[0], coords[1], this.symbol);
			this.options.changeActivity();
			if (game.checkDraw()) {
				this.completeGame("It's draw.\n" + Printer.print(game.getField()) + "\n-1",
						"It's draw.\n" + Printer.print(game.getField()) + "\n-1");
				return;
			}
			if (game.checkWin(this.symbol)) {
				this.completeGame("\nYou win!\n" + Printer.print(game.getField()) + "\n-1",
								  "You lose...\n" + Printer.print(game.getField()) + "\n-1");
				return;
			}
			this.waitOpponent();
			this.sendMessageOponent("Your move\n" + Printer.print(game.getField()) + "\n0");
		}
	}

	private void completeGame(String yourMessage, String opponentMessage) throws IOException {
		this.sendMessageOponent(opponentMessage);
		this.sendMessageActiveCurrentConsole(yourMessage);
		this.resetSettings();
	}

	private void resetSettings() {
		this.options.reduceCountConnections(2);
		this.connections.clear();
		this.game.changeStatus(true);
	}

	private void sendMessageActiveCurrentConsole(String message) throws IOException {
		PrintWriter output = new PrintWriter(connections.get(this.playerID - 1).getOutputStream());
		output.println(message);
		output.flush();
	}

	private void sendMessageOponent(String message) throws IOException {
		PrintWriter output = null;
		if (this.playerID - 1 == 0) {
			output = new PrintWriter(connections.get(1).getOutputStream());
		} else {
			output = new PrintWriter(connections.get(0).getOutputStream());
		}
		output.println(message);
		output.flush();
	}

	private void makeAction(String coordStr) throws IOException {
		if (this.options.getActivity() == this.playerID) {
			this.makeMove(coordStr);
		} else {
			this.waitOpponent();
		}
	}

	private void readMessageOfClient(BufferedReader input) throws IOException {
		String message = null;
		while (game.getStatus() != true && (message = input.readLine()) != null) {
			if (options.getCountConnections() == 2) {
				this.makeAction(message);
			} else {
				this.sendMessageActiveCurrentConsole("Waiting for player...\n0");
			}
		}
	}

	@Override
	public void run() {
		try {
			BufferedReader input = new BufferedReader(
					new InputStreamReader(connections.get(this.playerID - 1).getInputStream()));
			this.name = input.readLine();
			
			System.out.println("user " + this.name + " is now connected to the server...");
			this.readMessageOfClient(input);
			System.out.println("user " + this.name + " is now disconnected to the server...");
		} catch (IOException e) {
			System.out.println("user " + this.name + " is now disconnected to the server...");
		}
	}
}