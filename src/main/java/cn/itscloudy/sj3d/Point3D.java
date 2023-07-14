package cn.itscloudy.sj3d;

public class Point3D {
    protected float x;
    protected float y;
    protected float z;

    public Point3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * compare by z. Sort from the deepest to shallowest
     */
    public int compareByZ(Point3D another) {
        return Float.compare(another.z, z);
    }
}
