package intervalColoring;


public class Color {
    private int color;
    private int cost;
    public Color(int color, int cost){
        this.color = color;
        this.cost = cost;
    }

    public int getColor() {
        return color;
    }

    public int getCost() {
        return cost;
    }
}
