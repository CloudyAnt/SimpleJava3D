package cn.itscloudy;

class PxPoint {
    int x;
    int y;

    PxPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "PxPoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public static PxPoint fromXyz(double x, double y, double z) {
        return new PxPoint(SimpleJava3D.canvas.toPxX(x, z), SimpleJava3D.canvas.toPxY(y, z));
    }
}
