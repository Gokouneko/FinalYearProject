package intervalColoring;

import java.io.Serializable;

public class ColoredInterval implements Serializable {
    private Interval interval;
    private Color color;

    public ColoredInterval(Interval interval, Color color){
        this.interval = interval;
        this.color = color;
    }

    public int getLength(){
        return interval.getLength();
    }
    public Color getColor(){
        return color;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

