package cn.itscloudy.sj3d;

import java.util.ArrayList;
import java.util.List;

public class Obj {

    public final Point3D representative;
    private List<RelationalPoint> relationalPoints;
    public Obj(Point3D representative) {
        this.representative = representative;
        relationalPoints = new ArrayList<>();
    }

    public void addPoint(float xOffs, float yOffs, float zOffs) {
        relationalPoints.add(new RelationalPoint(xOffs, yOffs, zOffs));
    }

    public void addPoint(Point3D p) {
        relationalPoints.add(new RelationalPoint(
                p.x - representative.x,
                p.y - representative.y,
                p.z - representative.z));
    }

    private class RelationalPoint {
        private final float xOffs;
        private final float yOffs;
        private final float zOffs;
        private float x;
        private float y;
        private float z;
        private RelationalPoint(float xOffs, float yOffs, float zOffs) {
            this.xOffs = xOffs;
            this.yOffs = yOffs;
            this.zOffs = zOffs;
            reset();
        }

        private void reset() {
            x = representative.x + xOffs;
            y = representative.y + yOffs;
            z = representative.z + zOffs;
        }
    }
}
