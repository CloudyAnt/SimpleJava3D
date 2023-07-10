package cn.itscloudy;

import cn.itscloudy.util.Range;

import java.awt.*;
import java.util.List;

public class Line {
    private final Point a, b;
    double x1, y1, x2, y2, z1, z2;
    double minX, maxX;

    _2DLineFunc xyLineFunc;
    _2DLineFunc xzLineFunc;
    _2DLineFunc yzLineFunc;

    public Line(Point a, Point b) {
        this.a = a;
        this.b = b;
        reset();
    }

    public void reset() {
        this.x1 = a.x;
        this.y1 = a.y;
        this.z1 = a.z;
        this.x2 = b.x;
        this.y2 = b.y;
        this.z2 = b.z;
        xyLineFunc = new _2DLineFunc(x1, y1, x2, y2);
        xzLineFunc = new _2DLineFunc(x1, z1, x2, z2);
        yzLineFunc = new _2DLineFunc(y1, z1, y2, z2);
        if (x1 < x2) {
            minX = x1;
            maxX = x2;
        } else {
            minX = x2;
            maxX = x1;
        }
    }

    public PxLine toPxLine() {
        Canvas cvs = SimpleJava3D.canvas;
        return new PxLine(cvs.toPxX(x1, z1), cvs.toPxY(y1, z1), a.name,
                cvs.toPxX(x2, z2), cvs.toPxY(y2, z2), b.name);
    }

    static void toPxPoints(List<Point> container, double x1, double y1, double z1,
                           double x2, double y2, double z2, Color c) {
        _2DLineFunc xyLineFunc = new _2DLineFunc(x1, y1, x2, y2);
        _2DLineFunc xzLineFunc = new _2DLineFunc(x1, z1, x2, z2);
        _2DLineFunc yzLineFunc = new _2DLineFunc(y1, z1, y2, z2);
        double minX, maxX;
        if (x1 < x2) {
            minX = x1;
            maxX = x2;
        } else {
            minX = x2;
            maxX = x1;
        }

        toPxPoints(container, xyLineFunc, xzLineFunc, yzLineFunc, minX, maxX, c);
    }

    static void toPxPoints(List<Point> container, _2DLineFunc xyF, _2DLineFunc xzF, _2DLineFunc yzF,
                           double minXD, double maxXD, Color c) {
        int minX = (int) minXD;
        int maxX = (int) maxXD;
        if (xyF.perpendicular) {
            Range<Integer> yRange = xyF.heightRange;
            Integer yMin = yRange.getMin();
            Integer yMax = yRange.getMax();
//            if (c == Color.GREEN) {
//                System.out.println("1>> " + yMin + " - " + yMax);
//            }
            for (double y = yMin; y <= yMax; y += Quality.current.lineWidth) {
                double z = yzF.getDvByIv(y);
                container.add(new Point(maxX, y, z));
            }
        } else {
//            if (c == Color.GREEN) {
//                System.out.println("2>> " + minX + " - " + maxX);
//            }
            for (double x = minX; x <= maxX; x += Quality.current.lineWidth) {
                double y = xyF.getDvByIv(x);
                double z = xzF.getDvByIv(x);
                container.add(new Point(x, y, z));
            }
        }
    }

    // TODO handle perpendicular, X-Y perpendicular in exclusive method
}