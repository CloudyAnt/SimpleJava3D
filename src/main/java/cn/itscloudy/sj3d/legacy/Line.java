package cn.itscloudy.sj3d.legacy;

import cn.itscloudy.sj3d.LineFunc2D;
import cn.itscloudy.sj3d.util.Range;

import java.util.List;

public class Line {
    final SatellitePoint a, b;
    double x1, y1, x2, y2, z1, z2;
    double minX, maxX;

    LineFunc2D xyLineFunc;
    LineFunc2D xzLineFunc;
    LineFunc2D yzLineFunc;

    boolean xPerp;
    boolean yPerp;
    boolean zPerp;

    public Line(SatellitePoint a, SatellitePoint b) {
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

        xyLineFunc = new LineFunc2D((float) x1, (float) y1, (float) x2, (float) y2);
        xPerp = xyLineFunc.perpendicular;
        xzLineFunc = new LineFunc2D((float) x1, (float) z1, (float) x2, (float) z2);
        yzLineFunc = new LineFunc2D((float) y1, (float) z1, (float) y2, (float) z2);
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
        Canvas cvs = RotatableCube.canvas;
        return new PxLine(cvs.toPxX(x1, z1), cvs.toPxY(y1, z1), a.name,
                cvs.toPxX(x2, z2), cvs.toPxY(y2, z2), b.name);
    }

    static void scanLinePoints(List<SatellitePoint> container, SatellitePoint a, SatellitePoint b) {
        scanLinePoints(container, (float) a.x, (float) a.y, (float) a.z, (float) b.x, (float) b.y, (float) b.z);
    }

    static void scanLinePoints(List<SatellitePoint> container, float x1, float y1, float z1,
                               float x2, float y2, float z2) {
        LineFunc2D xyLineFunc = new LineFunc2D(x1, y1, x2, y2);
        LineFunc2D xzLineFunc = new LineFunc2D(x1, z1, x2, z2);
        LineFunc2D yzLineFunc = new LineFunc2D(y1, z1, y2, z2);
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

    static void scanLinePoints(List<SatellitePoint> container, LineFunc2D xyF, LineFunc2D xzF, LineFunc2D yzF,
                               double minXD, double maxXD) {
        int minX = (int) Math.round(minXD);
        int maxX = (int) Math.round(maxXD);
        if (xyF.perpendicular) {
            // line ⊥ X
            Range<Integer> yRange = xyF.dvIRange;
            Integer yMin = yRange.getMin();
            Integer yMax = yRange.getMax();
            if (yzF.perpendicular) {
                // line ⊥ X-Y
                Range<Integer> zRange = yzF.dvIRange;
                float minZ = zRange.getMin();
                float maxZ = zRange.getMax();
                for (float z = minZ; z <= maxZ; z += Quality.current.lineWidth) {
                    container.add(new SatellitePoint(minXD, (double) yMin, z));
                }
            } else {
                for (float y = yMin; y <= yMax; y += Quality.current.lineWidth) {
                    float z = yzF.getDvByIv(y);
                    container.add(new SatellitePoint(minX, y, z));
                }
            }
        } else {
            for (float x = minX; x <= maxX; x += Quality.current.lineWidth) {
                float y = xyF.getDvByIv(x);
                float z = xzF.getDvByIv(x);
                container.add(new SatellitePoint(x, y, z));
            }
        }
    }
}