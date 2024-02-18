import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerMethodsI extends Remote {
    // String sayHello() throws RemoteException;

    // расчет длины отрезка между точками;
    Double getDistance(Point a, Point b) throws RemoteException;
    // расчет длины окружности, центром которой является одна из точек, а радиусом – расстояние между точками;
    Double getCircleLengthByR(Point a, Point b) throws RemoteException;
    // расчет длины окружности, диаметром которой является расстояние между точками;
    Double getCircleLengthByD(Point a, Point b) throws RemoteException;
    // расчет площади круга, центром которого является одна из точек, а радиусом – расстояние между точками;
    Double getAreaByR(Point a, Point b) throws RemoteException;
    // расчет площади круга, диаметром которого является расстояние между точками.
    Double getAreaByD(Point a, Point b) throws RemoteException;
}