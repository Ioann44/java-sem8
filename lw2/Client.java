import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {

    private Client() {
    }

    private static void doTest(ServerMethodsI provider, Point a, Point b) throws RemoteException {
        System.out.println("Point A: x=" + a.getX() + ", y=" + a.getY());
        System.out.println("Point B: x=" + b.getX() + ", y=" + b.getY());
        System.out.println("Distance = " + provider.getDistance(a, b));
        System.out.println("Circle perimeter length by R = " + provider.getCircleLengthByR(a, b));
        System.out.println("Circle perimeter length by D = " + provider.getCircleLengthByD(a, b));
        System.out.println("Circle area by R = " + provider.getAreaByR(a, b));
        System.out.println("Circle area by D = " + provider.getAreaByD(a, b));
    }

    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ServerMethodsI stub = (ServerMethodsI) registry.lookup("PointsServer");

            // doTest(stub, new Point(-1, -2), new Point(3, -5));
            Point a, b;
            a = new Point(0, 0);
            b = new Point(0, 0);
            String inputLine = "";
            String[] numsInStr;
            System.out.println("Enter four doubles separated by spaces");
            System.out.println("for two points as: x1 y1 x2 y2");
            System.out.println("To exit enter any invalid data");
            // example: -1 -2 3 -5
            Scanner scanner = new Scanner(System.in);
            while (true) {
                try {
                    System.out.println("*******************************");
                    inputLine = scanner.nextLine();
                    numsInStr = inputLine.split("\\s+");
                    if (numsInStr.length != 4) {
                        throw new RuntimeException("Input must consist of 4 numbers");
                    }
                    {
                        a.setX(Double.parseDouble(numsInStr[0]));
                        a.setY(Double.parseDouble(numsInStr[1]));
                        b.setX(Double.parseDouble(numsInStr[2]));
                        b.setY(Double.parseDouble(numsInStr[3]));
                        doTest(stub, a, b);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid double format. Please enter valid doubles.");
                    break;
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                    break;
                }
            }
            scanner.close();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}