package cn.itscloudy;

import cn.itscloudy.util.Range;

import java.util.List;

public class Line {
    final Point a, b;
    double x1, y1, x2, y2, z1, z2;
    double minX, maxX;

    _2DLineFunc xyLineFunc;
    _2DLineFunc xzLineFunc;
    _2DLineFunc yzLineFunc;

    boolean xPerp;
    boolean yPerp;
    boolean zPerp;

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

        xPerp = x1 == x2;
        yPerp = y1 == y2;
        zPerp = z1 == z2;

        xyLineFunc = new _2DLineFunc(x1, y1, x2, y2);
        xPerp = xyLineFunc.perpendicular;
        xzLineFunc = new _2DLineFunc(x1, z1, x2, z2);
        yzLineFunc = new _2DLineFunc(y1, z1, y2, z2);
        yPerp = yzLineFunc.perpendicular;

        zPerp = Math.round(z1) == Math.round(z2);
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
    static void scanLinePoints(List<Point> container, Point a, Point b) {
        scanLinePoints(container, a.x, a.y, a.z, b.x, b.y, b.z);
    }

    static void scanLinePoints(List<Point> container, double x1, double y1, double z1,
                               double x2, double y2, double z2) {
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

        scanLinePoints(container, xyLineFunc, xzLineFunc, yzLineFunc, minX, maxX);
    }

    static void scanLinePoints(List<Point> container, _2DLineFunc xyF, _2DLineFunc xzF, _2DLineFunc yzF,
                               double minXD, double maxXD) {
        int minX = (int) Math.round(minXD);
        int maxX = (int) Math.round(maxXD);
        if (xyF.perpendicular) {
            // line ⊥ X
            Range<Integer> yRange = xyF.dvRange;
            Integer yMin = yRange.getMin();
            Integer yMax = yRange.getMax();
            if (yzF.perpendicular) {
                // line ⊥ X-Y
                Range<Integer> zRange = yzF.dvRange;
                double minZ = zRange.getMin();
                double maxZ = zRange.getMax();
                for (double z = minZ; z <= maxZ; z += Quality.current.lineWidth) {
                    container.add(new Point(minXD, (double) yMin, z));
                }
            } else {
                for (double y = yMin; y <= yMax; y += Quality.current.lineWidth) {
                    double z = yzF.getDvByIv(y);
                    container.add(new Point(minX, y, z));
                }
            }
        } else {
            for (double x = minX; x <= maxX; x += Quality.current.lineWidth) {
                double y = xyF.getDvByIv(x);
                double z = xzF.getDvByIv(x);
                container.add(new Point(x, y, z));
            }
        }
    }
}