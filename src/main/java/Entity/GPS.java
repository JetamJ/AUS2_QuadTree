package Entity;

public class GPS {

    private char width;
    private double x;
    private char height;
    private double y;

    public GPS(char width, double x, char height, double y){
        this.width = width;
        this.x = x;
        this.height = height;
        this.y = y;
    }

    public char getWidth() {
        return width;
    }

    public double getX() {
        return x;
    }

    public char getHeight() {
        return height;
    }

    public double getY() {
        return y;
    }

    public boolean equals(GPS gps) {
        if (this.getX() == gps.getX()
            && this.getY() == gps.getY()
            && this.width == gps.width
            && this.height == gps.height){
            return true;
        }
        return false;
    }
}
