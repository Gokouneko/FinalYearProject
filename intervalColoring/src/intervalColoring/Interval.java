package intervalColoring;

import java.io.Serializable;

public class Interval implements Serializable {
    private int[] coordinate;
    private int length;

    public Interval(int[] coordinate) {
        this.coordinate = coordinate;
        this.length = coordinate[coordinate.length-1] - coordinate[0];
    }

    public int[] getCoordinate() {
        return coordinate;
    }

    public int getLength() {
        return length;
    }

}
