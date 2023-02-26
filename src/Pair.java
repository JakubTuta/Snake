public class Pair<X, Y> {
    public int coordX;
    public int coordY;

    public Pair(int coordX, int coordY) {
        this.coordX = coordX;
        this.coordY = coordY;
    }

    public int getX() {
        return this.coordX;
    }

    public void setX(int value) {
        this.coordX = value;
    }

    public int getY() {
        return this.coordY;
    }

    public void setY(int value) {
        this.coordY = value;
    }
}