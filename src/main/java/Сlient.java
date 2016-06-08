import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Сlient {
	Socket connection;
	String userName;
	String host;
	int port;
	
	public Сlient(String userName, String host, int port) {
		this.userName = userName;
		this.host = host;
		this.port = port;
	}
	
	public void createConnection() {
		try {
			this.connection = new Socket(host, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startGame() {
		new Thread(new ClientThread(this.userName, connection)).start();
	}
}
