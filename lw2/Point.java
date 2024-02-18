import java.io.Serializable;

public class Point implements Serializable {
	private double x;
	private double y;

	// constructor
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	// getters
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	// setters
	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	// equals
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Point point = (Point) o;
		return x == point.x && y == point.y;
	}
}
