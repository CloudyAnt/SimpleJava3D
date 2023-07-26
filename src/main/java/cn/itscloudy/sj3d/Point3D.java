package cn.itscloudy.sj3d;

public class Point3D implements FloatXYZ {
    public float x;
    public float y;
    public float z;

    public Point3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int compareByZ(Point3D another) {
        return Float.compare(another.z, z);
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getZ() {
        return z;
    }
}
