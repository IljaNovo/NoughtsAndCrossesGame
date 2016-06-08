import java.awt.HeadlessException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.omg.CORBA.portable.UnknownException;

public class Server {
	private static final int PORT = 4444;
	private ServerSocket server;
	
	public void startServer() {
		try {
			server = new ServerSocket(PORT);
			System.out.println("Server is started...");
			while(true) {
				Socket connection = server.accept();
				new Thread(new ServerThread(connection)).start();
			}
			
		} catch(UnknownException e) {
			
		} catch(HeadlessException e) {
			
		} catch(IOException e) {
			
		}
		
	}
	
}
