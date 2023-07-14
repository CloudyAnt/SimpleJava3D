package cn.itscloudy.sj3d.legacy;

import cn.itscloudy.sj3d.FixedPoint3D;

import java.awt.*;
import java.util.Objects;

public class SatellitePoint implements Comparable<SatellitePoint> {
    String name;
    double x;
    double y;
    double z;

    double disToCenterSquare;
    double zyDis;
    double xzDis;
    double xyDis;
    double zyDegree;
    double xzDegree;
    double xyDegree;

    Color color;

    FixedPoint3D center = RotatableCube.center;

    public SatellitePoint(double x, double y, double z, String name) {
        this.name = name;
        color = Color.BLACK;
        reset(x, y, z);
    }

    public SatellitePoint(double x, double y, double z) {
        this(x, y, z, "");
    }

    public void reset(double x, double y, double z) {
        this.x = format(x);
        this.y = format(y);
        this.z = format(z);
        // TODO calculate these when turn
        zyDis = Math.sqrt(z(2) + y(2));
        xzDis = Math.sqrt(x(2) + z(2));
        xyDis = Math.sqrt(x(2) + y(2));
        disToCenterSquare = x(2) + y(2) + z(2);

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
        xzDegree = addDegree(degree, xzDegree);
        double cos = Math.cos(Math.toRadians(xzDegree));
        double sin = Math.sin(Math.toRadians(xzDegree));

        x = format(xzDis * cos + center.x);
        z = format(xzDis * sin + center.z);

        zyDis = Math.sqrt(disToCenterSquare - x(2));
        xyDis = Math.sqrt(disToCenterSquare - z(2));
        zyDegree = degreeFromAB(y(1), z(1));
        xyDegree = degreeFromAB(y(1), x(1));
    }

    public void xyTurn(int degree) {
        xyDegree = addDegree(degree, xyDegree);

        double cos = Math.cos(Math.toRadians(xyDegree));
        double sin = Math.sin(Math.toRadians(xyDegree));

        x = format(xyDis * cos + center.x);
        y = format(xyDis * sin + center.y);

        zyDis = Math.sqrt(disToCenterSquare - x(2));
        xzDis = Math.sqrt(disToCenterSquare - y(2));
        zyDegree = degreeFromAB(y(1), z(1));
        xzDegree = degreeFromAB(z(1), x(1));
    }

    public void zyTurn(int degree) {
        zyDegree = addDegree(degree, zyDegree);

        double cos = Math.cos(Math.toRadians(zyDegree));
        double sin = Math.sin(Math.toRadians(zyDegree));

        z = format(zyDis * cos + center.z);
        y = format(zyDis * sin + center.y);

        xzDis = Math.sqrt(disToCenterSquare - y(2));
        xyDis = Math.sqrt(disToCenterSquare - z(2));
        xzDegree = degreeFromAB(z(1), x(1));
        xyDegree = degreeFromAB(y(1), x(1));
    }

    private int addDegree(int degree, double origin) {
        return degree % 360 + (int) origin;
    }

    private double format(double d) {
        return d;
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
        SatellitePoint point = (SatellitePoint) o;
        return Double.compare(point.x, x) == 0 &&
                Double.compare(point.y, y) == 0 &&
                Double.compare(point.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public int compareTo(SatellitePoint o) {
        return Double.compare(o.z, z);
    }

    int toPxX() {
        return RotatableCube.canvas.toPxX(x, z);
    }

    int toPxY() {
        return RotatableCube.canvas.toPxY(y, z);
    }
}
