import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerParallel {
	public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(8081)) {
			while (true) {
				Socket clientSocket = serverSocket.accept();

				new Thread(() -> {
					try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
							ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

						while (true) {
							Point point = (Point) in.readObject();

							if (point.equals(new Point(Integer.MAX_VALUE, Integer.MAX_VALUE))) {
								break;
							}

							Point response;

							if (point.y != 0) {
								int quotient = point.x / point.y;
								int remainder = point.x % point.y;
								response = new Point(quotient, remainder);
							} else {
								response = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
							}

							out.writeObject(response);
						}

					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				}).start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
