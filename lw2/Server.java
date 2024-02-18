import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Server implements ServerMethodsI {
	public static void main(String args[]) {
		System.setProperty("java.rmi.server.hostname", "localhost");

		try {
			Server obj = new Server();
			ServerMethodsI stub = (ServerMethodsI) UnicastRemoteObject.exportObject(obj, 0);

			Registry registry = LocateRegistry.createRegistry(1099);
			registry.bind("PointsServer", stub);

			System.err.println("Server ready");
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}

	public Double getDistance(Point a, Point b) {
		double dist = Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
		return dist;
	}

	public Double getCircleLengthByR(Point a, Point b) {
		return 2 * Math.PI * getDistance(a, b);
	}

	public Double getCircleLengthByD(Point a, Point b) {
		return getCircleLengthByR(a, b) / 2;
	}

	public Double getAreaByR(Point a, Point b) {
		return Math.PI * Math.pow(getDistance(a, b), 2);
	}

	public Double getAreaByD(Point a, Point b) {
		return getAreaByR(a, b) / 4;
	}
}