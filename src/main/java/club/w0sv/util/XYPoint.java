package club.w0sv.util;

public class XYPoint {
    public static final XYPoint ORIGIN = new XYPoint(0, 0);
    public static final XYPoint NO_OFFSET = ORIGIN;
    
    private final int x;
    private final int y;

    public XYPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "<" + x + "," + y + ">";
    }
}
