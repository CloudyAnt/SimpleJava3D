package cn.itscloudy.sj3d.legacy;

import cn.itscloudy.sj3d.LineFunc2D;

public class Line {
    final SatellitePoint a, b;
    double x1, y1, x2, y2, z1, z2;

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
    }

    public PxLine toPxLine() {
        Canvas cvs = Canvas.instance;
        return new PxLine(cvs.toPxX(x1, z1), cvs.toPxY(y1, z1), a.name,
                cvs.toPxX(x2, z2), cvs.toPxY(y2, z2), b.name);
    }
}