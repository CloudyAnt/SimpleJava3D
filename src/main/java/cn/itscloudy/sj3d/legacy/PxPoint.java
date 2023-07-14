package cn.itscloudy.sj3d.legacy;

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

}
