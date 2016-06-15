
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	public static void main(String[] args) {
		String name = args[0];
		try {
			Socket socket = new Socket("localhost", 4444);
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
			System.out.println(input.readLine());
			if(input.readLine() == "1") {
				try {
					Thread.currentThread().sleep(5000);
					return;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			output.println(name);
			output.flush();
			BufferedReader inputConsole = new BufferedReader(new InputStreamReader(System.in));
			while(true) {
				while(true) {
					String message = input.readLine();
					if (message.equals("0")) {
						break;
					}
					System.out.println(message);
				}
				String readerInput = inputConsole.readLine();
				output.println(readerInput);
				output.flush();
			}
		} catch (UnknownHostException e) {
			System.out.println("host is not found");
		} catch (IOException e) {
			System.out.println("cannot to conect to the server");
		}
	}
}
