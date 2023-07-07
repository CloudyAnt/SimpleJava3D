package cn.itscloudy;

import cn.itscloudy.util.Range;

import java.util.ArrayList;
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

    public PxLint toPxLine() {
        Canvas cvs = SimpleJava3D.canvas;
        return new PxLint(cvs.toPxX(x1, z1), cvs.toPxY(y1, z1), a.name,
                cvs.toPxX(x2, z2), cvs.toPxY(y2, z2), b.name);
    }

    public List<PxPoint> getPxPointsInXRange() {
        List<PxPoint> pxPoints = new ArrayList<>();
        if (xyLineFunc.perpendicular) {
            Range<Double> yRange = xyLineFunc.heightRange;
            Double yMin = yRange.getMin();
            Double yMax = yRange.getMax();
            for (double y = yMin; y <= yMax; y += 50) {
                double z = yzLineFunc.getByParam(y);
                pxPoints.add(PxPoint.fromXyz(x1, y, z));
            }
        } else {
            for (double x = minX; x <= maxX; x += 50) {
                double y = xyLineFunc.getByParam(x);
                double z = xzLineFunc.getByParam(x);
                pxPoints.add(PxPoint.fromXyz(x, y, z));
            }
        }
        return pxPoints;
    }
}