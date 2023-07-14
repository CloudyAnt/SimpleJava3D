package cn.itscloudy.sj3d.legacy;

public class PxLine {
    String name1;
    String name2;
    int x1;
    int y1;
    int x2;
    int y2;

    public PxLine(int x1, int y1, int x2, int y2) {
        this(x1, y1, null, x2, y2, null);
    }

    public PxLine(int x1, int y1, String name1, int x2, int y2, String name2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.name1 = name1;
        this.name2 = name2;
    }
}
