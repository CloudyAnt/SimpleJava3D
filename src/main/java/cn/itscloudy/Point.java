package cn.itscloudy;

import java.util.Objects;

public class Point {
    String name;
    double x;
    double y;
    double z;

    static Point center = new Point() {{
        x = 0;
        y = 0;
        z = (double) (SimpleJava3D.closeSurfaceDis + SimpleJava3D.farSurfaceDis) / 2;
    }};

    double dis2;
    double zyDis;
    double xzDis;
    double xyDis;
    double zyDegree;
    double xzDegree;
    double xyDegree;

    public Point() {
    }

    public Point(double x, double y, double z, String name) {
        this.name = name;
        reset(x, y, z);
    }

    public Point(double x, double y, double z) {
        this(x, y, z, "-");
    }

    public void reset(double x, double y, double z) {
        this.x = format(x);
        this.y = format(y);
        this.z = format(z);
        // TODO calculate these when turn
        zyDis = Math.sqrt(z(2) + y(2));
        xzDis = Math.sqrt(x(2) + z(2));
        xyDis = Math.sqrt(x(2) + y(2));
        dis2 = x(2) + y(2) + z(2);

        zyDegree = degreeFromAB(y(1), z(1));
        xzDegree = degreeFromAB(z(1), x(1));
        xyDegree = degreeFromAB(y(1), x(1));
    }

    private int degreeFromAB(double a, double b) {
        double degree = Math.toDegrees(Math.atan(a / b));
        if (b < 0) {
            return 180 + (int) degree;
        }
        return (int) degree;
    }

    private double z(int n) {
        double t = 1;
        for (int i = 0; i < n; i++) {
            t *= (z - center.z);
        }
        return t;
    }

    private double x(int n) {
        double t = 1;
        for (int i = 0; i < n; i++) {
            t *= (x - center.x);
        }
        return t;
    }

    private double y(int n) {
        double t = 1;
        for (int i = 0; i < n; i++) {
            t *= (y - center.y);
        }
        return t;
    }

    public void xzTurn(int degree) {
//        if (center == null) {
//            return;
//        }
//        if (!center.equals(this.center)) {
//            this.center = center;
//
//            zyDis = Math.sqrt(z(2) + y(2));
//            xyDis = Math.sqrt(x(2) + y(2));
//
//            zyDegree = (int) degreeFromAB(y(1), z(1));
//            xyDegree = (int) degreeFromAB(y(1), x(1));
//        }

        xzDegree = addDegree(degree, xzDegree);
        double cos = Math.cos(Math.toRadians(xzDegree));
        double sin = Math.sin(Math.toRadians(xzDegree));

        x = format(xzDis * cos + center.x);
        z = format(xzDis * sin + center.z);
        //
        zyDis = Math.sqrt(dis2 - x(2));
        xyDis = Math.sqrt(dis2 - z(2));
        zyDegree = degreeFromAB(y(1), z(1));
        xyDegree = degreeFromAB(y(1), x(1));
    }

    public void xyTurn(int degree) {
        xyDegree = addDegree(degree, xyDegree);

        double cos = Math.cos(Math.toRadians(xyDegree));
        double sin = Math.sin(Math.toRadians(xyDegree));

        x = format(xyDis * cos + center.x);
        y = format(xyDis * sin + center.y);

        zyDis = Math.sqrt(dis2 - x(2));
        xzDis = Math.sqrt(dis2 - y(2));
        zyDegree = degreeFromAB(y(1), z(1));
        xzDegree = degreeFromAB(z(1), x(1));
    }

    public void zyTurn(int degree) {
        zyDegree = addDegree(degree, zyDegree);

        double cos = Math.cos(Math.toRadians(zyDegree));
        double sin = Math.sin(Math.toRadians(zyDegree));

        z = format(zyDis * cos + center.z);
        y = format(zyDis * sin + center.y);

        xzDis = Math.sqrt(dis2 - y(2));
        xyDis = Math.sqrt(dis2 - z(2));
//        xzDis = Math.sqrt(x(2) + z(2));
//        xyDis = Math.sqrt(x(2) + y(2));
        xzDegree = degreeFromAB(z(1), x(1));
        xyDegree = degreeFromAB(y(1), x(1));
    }

    private int addDegree(int degree, double origin) {
        return degree % 360 + (int) origin;
    }

    private double format(double d) {
        return d;
//        return (int) (d * 1000) / (double) 1000;
    }

    @Override
    public String toString() {
        return name + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", xCenter=" + center.x +
                ", yCenter=" + center.y +
                ", zCenter=" + center.z +
                ", zyDis=" + zyDis +
                ", xzDis=" + xzDis +
                ", xyDis=" + xyDis +
                ", zyDegree=" + zyDegree +
                ", xzDegree=" + xzDegree +
                ", xyDegree=" + xyDegree +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 &&
                Double.compare(point.y, y) == 0 &&
                Double.compare(point.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
