
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
			
			output.println(name);
			output.flush();
			
			BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
			
			while(true) {
				readMessageOfServer(input, output, socket);
				String readerInput = consoleInput.readLine();
				output.println(readerInput);
				output.flush();
			}
		} catch (UnknownHostException e) {
			System.out.println("host is not found");
		} catch (IOException e) {
			System.out.println("cannot to conect to the server");
		}
	}
	
	private static void closeConnection(Socket socket, BufferedReader input, 
		PrintWriter output) throws IOException {
		try {
			socket.close();
			input.close();
			output.close();
			Thread.currentThread().sleep(5000);
			System.exit(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static void readMessageOfServer(BufferedReader input, PrintWriter output, Socket socket) throws IOException {
		while(true) {
			String message = input.readLine();
			if (message.equals("0")) {
				break;
			}
			if (message.equals("1")) {
				closeConnection(socket, input, output);
			}
			System.out.println(message);
		}
	}
}
