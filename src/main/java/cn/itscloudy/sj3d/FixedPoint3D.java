package cn.itscloudy.sj3d;

public class FixedPoint3D implements FloatXYZ {
    public final float x;
    public final float y;
    public final float z;

    public FixedPoint3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
