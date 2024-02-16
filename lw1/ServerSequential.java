import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSequential {
	public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(8080);
				Socket clientSocket = serverSocket.accept();
				ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

			while (true) {
				Point point = (Point) in.readObject();

				if (point.equals(new Point(Integer.MAX_VALUE, Integer.MAX_VALUE))) {
					break;
				}

				if (point.y != 0) {
					int quotient = point.x / point.y;
					int remainder = point.x % point.y;
					out.writeObject(new Point(quotient, remainder));
				} else {
					out.writeObject(new Point(Integer.MIN_VALUE, Integer.MIN_VALUE));
				}
			}

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}