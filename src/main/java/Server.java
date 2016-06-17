
import java.awt.HeadlessException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.omg.CORBA.portable.UnknownException;

public class Server {
	public static final int PORT = 4444;
	
	public static void main(String[] args) {
		Game game = new Game(3);
		Options options = new Options();
		List<Socket> connections = new ArrayList<Socket>();
		
		try {
			ServerSocket server = new ServerSocket(PORT);
			System.out.println("Server is started...");
			
			int count = 0;
			while(true) {
				Socket connection = server.accept();
				options.increaseCountConnections(1);
				if (options.getCountConnections() > 2) {
					sendTo(connection.getOutputStream(), "Sorry, server is busy\n1");
					options.reduceCountConnections(1);
				} else {
					connections.add(connection);
					initializeGame(game, options);
					sendTo(connection.getOutputStream(), "Successful connection!");
					sendNotification(connections, game);
					new Thread(new ServerThread(connections, game, options)).start();
				}
			}
			
		} catch(UnknownException e) {
			e.printStackTrace();
		} catch(HeadlessException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void initializeGame(Game game, Options options) {
		game.changeStatus(false);
		game.clearField();
		options.setActivity(1);
	}
	
	private static void sendNotification(List<Socket> connections, Game game) throws IOException {
		if (connections.size() == 1) {
			sendTo(connections.get(0).getOutputStream(),
					"Waiting for player...");
		}
		if (connections.size() == 2) {
			sendTo(connections.get(0).getOutputStream(),
					"Start game!\nYour sympol is " + Symbols.cross.name() + ".\n\nYour move\n" + Printer.print(game.getField()) + "\n0");
			
			sendTo(connections.get(1).getOutputStream(),
					"Start game!\nYour sympol is " + Symbols.nought.name() + ".\n\nMove opponent...\n" + Printer.print(game.getField()));
		}
	}	

	private static void sendTo(OutputStream outStream, String message) {
		PrintWriter out = new PrintWriter(outStream, true);
		out.println(message);
		out.flush();
	}
}
