import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server implements Hello {

	public Server() {
	}

	public String sayHello() {
		return "Hello, world!";
	}

	public static void main(String args[]) {
		System.setProperty("java.rmi.server.hostname", "localhost");

		try {
			Server obj = new Server();
			Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, 0);

			Registry registry = LocateRegistry.createRegistry(1099);
			// registry.rebind("hello", stub);
			// Registry registry = LocateRegistry.getRegistry(1099);
			registry.bind("Hello", stub);

			System.err.println("Server ready");
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}
}