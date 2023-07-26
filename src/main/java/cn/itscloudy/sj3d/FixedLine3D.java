package cn.itscloudy.sj3d;

public class FixedLine3D {
    public final float x1;
    public final float y1;
    public final float z1;
    public final float x2;
    public final float y2;
    public final float z2;

    public final LineFunc2D xyLineFunc;
    public final LineFunc2D xzLineFunc;
    public final LineFunc2D yzLineFunc;

    public final boolean xPerp;
    public final boolean yPerp;
    public final boolean zPerp;

    public FixedLine3D(FloatXYZ xyz1, FloatXYZ xyz2) {
        this(xyz1.getX(), xyz1.getY(), xyz1.getZ(), xyz2.getX(), xyz2.getY(), xyz2.getZ());
    }

    public FixedLine3D(float x1, float y1, float z1, float x2, float y2, float z2) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;

        xyLineFunc = new LineFunc2D(x1, y1, x2, y2);
        xPerp = xyLineFunc.perpendicular;
        xzLineFunc = new LineFunc2D(x1, z1, x2, z2);
        yzLineFunc = new LineFunc2D(y1, z1, y2, z2);
        yPerp = yzLineFunc.perpendicular;

        zPerp = Math.round(z1) == Math.round(z2);
    }

    @Override
    public String toString() {
        return "[(" + x1 + "," + y1 + "," + z1 + "),(" + x2 + "," + y2 + "," + z2 + ")]";
    }
}
