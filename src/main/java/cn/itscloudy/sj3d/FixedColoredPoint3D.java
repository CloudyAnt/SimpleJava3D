package cn.itscloudy.sj3d;

import java.awt.*;

public class FixedColoredPoint3D extends FixedPoint3D {

    public Color color;

    public FixedColoredPoint3D(FloatXYZ xyz) {
        super(xyz.getX(), xyz.getY(), xyz.getZ());
    }
    public FixedColoredPoint3D(float x, float y, float z) {
        super(x, y, z);
        color = Color.BLACK;
    }

    public FixedColoredPoint3D(float x, float y, float z, Color color) {
        super(x, y, z);
        this.color = color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int compareByZ(Point3D another) {
        return Float.compare(another.z, z);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }
}
