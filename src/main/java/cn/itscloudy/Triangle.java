package cn.itscloudy;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class Triangle {

    private final Point c;
    private final Point b;
    private final Point a;

    List<Point> points;

    Color color;

    Triangle(Point a, Point b, Point c, Color color) {
        this.c = c;
        this.b = b;
        this.a = a;
        this.color = color;
        points = new ArrayList<>();
        reset();
    }

    void reset() {
        points.clear();
        // sort the 3 points by x
        Point p1, p2, p3;
        if (b.x > a.x) {
            p1 = a;
            p2 = b;
        } else {
            p1 = b;
            p2 = a;
        }
        if (c.x > p2.x) {
            p3 = c;
        } else {
            p3 = p2;
            p2 = c;
        }

        Line l1 = new Line(p1, p2);
        Line l2 = new Line(p2, p3);
        Line l3 = new Line(p1, p3);

        // p1.x to p2.x
        scan(p1.x, p2.x, l1, l3);
        // p2.x to p3.x
        scan(p2.x, p3.x, l2, l3);

        points.forEach(p -> p.color = color);
    }

    void scan(double from, double to, Line mainLine, Line coLine) {
        if (!mainLine.xyLineFunc.perpendicular) {
            for (double x = from; x < to; x += Quality.current.lineWidth) {
                double y = mainLine.xyLineFunc.getDvByIv(x);
                double z = mainLine.xzLineFunc.getDvByIv(x);
                double y0 = y, z0 = z;

                y = coLine.xyLineFunc.getDvByIv(x);
                z = coLine.xzLineFunc.getDvByIv(x);

                Line.toPxPoints(points, x, y0, z0, x, y, z);
            }
        }
    }
}
