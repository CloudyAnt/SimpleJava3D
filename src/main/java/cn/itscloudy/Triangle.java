package cn.itscloudy;

import cn.itscloudy.util.Range;

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
        scanOnX(p1.x, p2.x, l1, l3);
        // p2.x to p3.x
        scanOnX(p2.x, p3.x, l2, l3);

        points.forEach(p -> p.color = color);
    }

    void scanOnX(double fromD, double toD, Line mainLine, Line coLine) { // from = mainLine.x, to = coLine.x
        int from = (int) fromD;
        int to = (int) toD;
        if (mainLine.xyLineFunc.perpendicular) {
            Range<Integer> yRange = mainLine.xyLineFunc.heightRange;
            Integer minY = yRange.getMin();
            Integer maxY = yRange.getMax();
//            if (color == Color.GREEN) {
//                System.out.println("SCANNING x:" + from + " y range: " + minY + " - " + maxY);
//            }

//            Integer minCoZ = null;
//            Integer maxCoZ = null;
//            if (coLine.yzLineFunc.perpendicular) {
//                Range<Integer> coZRange = coLine.yzLineFunc.heightRange;
//                minCoZ = coZRange.getMin();
//                maxCoZ = coZRange.getMax();
//            }

            if (mainLine.yzLineFunc.perpendicular) {
                // mainLine perpendicular to X-Y surface
                Range<Integer> mainZRange = mainLine.yzLineFunc.heightRange;
                Integer minMainZ = mainZRange.getMin();
                Integer maxMainZ = mainZRange.getMax();
                Line.toPxPoints(points, from, minY, minMainZ, from, minY, maxMainZ, color);
            } else {
                for (double y = minY; y <= maxY; y += Quality.current.lineWidth) {
                    double z0 = mainLine.yzLineFunc.getDvByIv(y);
                    double z = coLine.yzLineFunc.getDvByIv(y);
                    Line.toPxPoints(points, from, y, z0, from, y, z, color);
                }
            }


        } else {
            if (color == Color.GREEN) {
                System.out.println("SCANNING from:" + from + " - " + to);
            }
            for (double x = from; x <= to; x += Quality.current.lineWidth) {
                double y = mainLine.xyLineFunc.getDvByIv(x);
                double z = mainLine.xzLineFunc.getDvByIv(x);

                double y1 = coLine.xyLineFunc.getDvByIv(x);
                double z1 = coLine.xzLineFunc.getDvByIv(x);

                Line.toPxPoints(points, x, y, z, x, y1, z1, color);
            }
        }
    }
}
