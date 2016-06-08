import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThread implements Runnable{
	Socket socket;

	public ServerThread(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try(BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			String message = null;
			while ((message = input.readLine()) != null) {
				System.out.println("client message: " + message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
