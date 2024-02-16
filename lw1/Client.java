import java.io.*;
import java.net.Socket;

public class Client {
	public static void main(String[] args) {
		try (Socket socket = new Socket("localhost", 8081); // 8080 - sequential, 8081 - parallel
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

			int[][] pointsArr = { { 1, 0 }, { 5, 3 }, { 20, 5 } };
			for (int[] arr : pointsArr) {
				int x = arr[0];
				int y = arr[1];

				out.writeObject(new Point(x, y));

				Point result = (Point) in.readObject();
				System.out.println("a = " + x + ", b = " + y);
				if (result.equals(new Point(Integer.MIN_VALUE, Integer.MIN_VALUE))) {
					System.out.println("Ошибка: деление на ноль.");
				} else {
					System.out.println("Div: " + result.x + ", Mod: " + result.y);
				}
				System.out.println();
			}

			out.writeObject(new Point(Integer.MAX_VALUE, Integer.MAX_VALUE));

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
