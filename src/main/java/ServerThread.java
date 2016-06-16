
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
	private Socket socket;
	private volatile Game game;
	private volatile Options options;
	private volatile Symbols symbol;
	private volatile int playerID;
	
	public ServerThread(Socket socket, Game game, Options options) {
		this.socket = socket;
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
		output.println("Move opponent...\n0");
		output.flush();
	}
	
	private void makeMove(String coordsStr, BufferedReader input, PrintWriter output) throws IOException {
		while(true) {
			int[] coords = Parser.parseCoords(coordsStr);
			if (!game.setSymbol(coords[0], coords[1], this.symbol)) {
				output.println("Call is not empty...\n0");
				output.flush();
				continue;
			}
			game.setSymbol(coords[0], coords[1], this.symbol);
			this.options.changeActivity();
			this.waitOpponent(input, output);
			break;
		}
	}
	
	private void sendInitialNotification(BufferedReader input, PrintWriter output) {
		if (options.getActivity() == this.playerID) {
			output.println("Your move!\n" + 
							Printer.print(this.game.getField()) +
							"\n0");
			output.flush();
		} else {
			output.println("Move opponent...\n0");
			output.flush();
		}
	}
	
	private void makeAction(String coordStr, BufferedReader input, PrintWriter output) throws IOException {
		if (this.options.getEndGame() == true) {
			return;
		}
		if (this.options.getActivity() == this.playerID) {
			this.makeMove(coordStr, input, output);
			this.checkWin();
			if (this.options.getStateWinOfOpponent() == true) {
				output.println("You win!\n0");
				this.options.setEndGame(true);
				output.flush();
			}
		} else {
			this.waitOpponent(input, output);
		}
	}
	
	private void checkWin() {
		if (this.game.checkWin(this.symbol)) {
			this.options.setStateWinOfOpponent(true);
		}
	}
	
	@Override
	public void run() {
		try {
			String message = null;
			PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			System.out.println("user" + input.readLine() + "is now connected to the server...");
			this.sendInitialNotification(input, output);
			
			while ((message = input.readLine()) != null) {
				this.sendInitialNotification(input, output);
				if (options.getCountConnections() == 2) {
					this.makeAction(message, input, output);
					this.sendInitialNotification(input, output);
					if (this.options.getStateWinOfOpponent() == true) {
						output.println("You lose...\n0");
						output.flush();
						break;
					}
				} else {
					output.println("Waiting for player...\n0");
					output.flush();
				}
			}
			Thread.currentThread().sleep(5000);
			socket.close();
		} catch (IOException e) {
			
		} catch (InterruptedException e) {
			
		}
	}
}
