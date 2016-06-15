
import java.awt.HeadlessException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import org.omg.CORBA.portable.UnknownException;

public class Server {
	public static final int PORT = 4444;
	
	public static void main(String[] args) {
		Game game = new Game(3);
		Options options = new Options();
		
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
					sendTo(connection.getOutputStream(), "Successful connection!\n0");
					new Thread(new ServerThread(connection, game, options)).start();
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
	
	private static void sendTo(OutputStream outStream, String message) {
		PrintWriter out = new PrintWriter(outStream, true);
		out.println(message);
		out.flush();
	}
}
