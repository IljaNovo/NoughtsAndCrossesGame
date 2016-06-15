
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
	
	@Override
	public void run() {
		try {
			String message = null;
			PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("user" + input.readLine() + "is now connected to the server...");
			output.println(Printer.print(this.game.getField()) + "\n0");
			output.flush();
			while ((message = input.readLine()) != null) {
				if (options.getCountConnections() == 2) {
					if (this.options.getActivity() == this.playerID) {
						int[] coords = Parser.parseCoords(input.readLine());
						game.setSymbol(coords[0], coords[1], this.symbol);
						this.options.changeActivity();
//						if(!game.setSymbol(coords[0], coords[1], this.symbol)) {
//							output.println("Call is not empty...\n0");
//						}
					} else {
						output.println("Move opponent...\n0");
						output.flush();
					}
				} else {
					output.println("Waiting for player...\n0");
					output.flush();
				}
			}
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
