import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread implements Runnable{

	Socket socket;
	String userName;
	
	public ClientThread(String userName, Socket socket) {
		this.socket = socket;
		this.userName = userName;
	}
	
	@Override
	public void run() {
		try(Scanner scan = new Scanner(System.in);
			PrintWriter output = new PrintWriter(this.socket.getOutputStream(), true)) {
			while(true) {
				String readerInput = scan.nextLine();
				output.flush();
				output.println(userName + ": " + readerInput);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
