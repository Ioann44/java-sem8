import java.io.Serializable;

public class Point implements Serializable {
	public int x;
	public int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

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
