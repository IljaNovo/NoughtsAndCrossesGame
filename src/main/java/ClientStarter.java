
public class ClientStarter {
	public static void main(String[] args) {
		Сlient client = new Сlient(args[0], "localhost", 4444);
		client.createConnection();
		client.startGame();
	}
}
