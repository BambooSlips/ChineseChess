package xyz.moment.here.model;

public class chessPoint {

    private int x;
    private int y;

    public chessPoint()
    {

    }

    public chessPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void move(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

}
