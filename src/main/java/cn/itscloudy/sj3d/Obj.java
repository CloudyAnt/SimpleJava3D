package cn.itscloudy.sj3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Obj extends Point3D {

    private final List<RelationalPoint> points;
    private final List<RelationalLine> lines;
    private float xyRad; // Z rotation
    private float xzRad; // Y rotation
    private float yzRad; // X rotation

    public Obj(float x, float y, float z) {
        super(x, y, z);
        points = new ArrayList<>();
        lines = new ArrayList<>();
    }

    /**
     * @param rx relative x
     * @param ry relative y
     * @param rz relative z
     */
    public void addPoint(float rx, float ry, float rz) {
        points.add(new RelationalPoint(rx, ry, rz));
    }

    public void addLine(float rx1, float ry1, float rz1, float rx2, float ry2, float rz2, String id) {
        lines.add(new RelationalLine(rx1, ry1, rz1, rx2, ry2, rz2, id));
    }

    public void addLine(float rx1, float ry1, float rz1, float rx2, float ry2, float rz2) {
        lines.add(new RelationalLine(rx1, ry1, rz1, rx2, ry2, rz2));
    }

    public void draw(World world, Graphics2D graphics2D) {
        for (RelationalPoint rp : points) {
            Point point = getPoint2D(world, rp);
            if (point == null) return;
            graphics2D.drawString(String.valueOf(rp.id), point.x, point.y);
        }
        for (RelationalLine line : lines) {
            Point p1 = getPoint2D(world, line.p1);
            Point p2 = getPoint2D(world, line.p2);
            if (p1 == null || p2 == null) {
                return;
            }
            graphics2D.drawString(line.id, (p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
            graphics2D.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    private Point getPoint2D(World world, RelationalPoint rp) {
        float x = this.x + rp.wx;
        float y = this.y + rp.wy;
        float z = this.z + rp.wz;
        return world.to2D(x, y, z);
    }

    public void rotateXy(int degree) {
        xyRad += Math.toRadians(degree);
        updateAll();
    }

    public void rotateXz(int degree) {
        xzRad += Math.toRadians(degree);
        updateAll();
    }

    public void rotateYz(int degree) {
        yzRad += Math.toRadians(degree);
        updateAll();
    }

    private void updateAll() {
        lines.forEach(l -> {
            l.p1.update();
            l.p2.update();
        });
        points.forEach(RelationalPoint::update);
    }

    public void restore() {
        xyRad = 0;
        xzRad = 0;
        yzRad = 0;
        updateAll();
    }

    public Obj copy(float x, float y, float z) {
        Obj o = new Obj(x, y, z);
        points.forEach(p -> p.copyTo(o));
        lines.forEach(l -> l.copyTo(o));
        return o;
    }

    private class RelationalPoint {
        private final String id;
        // ox, oy, oz is coordinates in perspective of Obj
        private final float ox;
        private final float oy;
        private final float oz;
        private final float oxyDis; // xy projection distance
        private final float oxzDis; // xz projection distance
        private final float oyzDis; // yz projection distance
        private final float oxyRad;
        private final float oyzRad;
        private final float oxzRad;
        // wx, wy, wz is coordinates in perspective of World (Obj as the origin)
        private float wx;
        private float wy;
        private float wz;
        private float wxyDis; // xy projection distance
        private float wxzDis; // xz projection distance
        private float wyzDis; // yz projection distance
        private float wxyRad;
        private float wxzRad;
        private float wyzRad;

        private RelationalPoint(float ox, float oy, float oz) {
            this(ox, oy, oz, ".");
        }

        private RelationalPoint(float ox, float oy, float oz, String id) {
            this.id = id;
            wx = this.ox = ox;
            wy = this.oy = oy;
            wz = this.oz = oz;

            double x2 = Math.pow(wx, 2);
            double y2 = Math.pow(wy, 2);
            oxyDis = (float) (Math.sqrt(x2 + y2));
            double z2 = Math.pow(wz, 2);
            oxzDis = (float) (Math.sqrt(x2 + z2));
            oyzDis = (float) (Math.sqrt(y2 + z2));

            oxyRad = (float) Math.atan2(oy, ox);
            oxzRad = (float) Math.atan2(oz, ox);
            oyzRad = (float) Math.atan2(oz, oy);
            restore();
        }

        private void copyTo(Obj obj) {
            obj.addPoint(ox, oy, oz);
        }

        private void restore() {
            wx = ox;
            wy = oy;
            wz = oz;
            wxyDis = oxyDis;
            wxzDis = oxzDis;
            wyzDis = oyzDis;
            wxyRad = oxyRad;
            wxzRad = oxzRad;
            wyzRad = oyzRad;
        }

        private void update() {
            restore();
            if (xyRad != 0) {
                wxyRad += xyRad;
                wx = (float) Math.cos(wxyRad) * wxyDis;
                wy = (float) Math.sin(wxyRad) * wxyDis;
                double z2 = Math.pow(wz, 2);
                wxzDis = (float) (Math.sqrt(Math.pow(wx, 2) + z2));
                wyzDis = (float) (Math.sqrt(Math.pow(wy, 2) + z2));
                wxzRad = (float) Math.atan2(wz, wx);
                wyzRad = (float) Math.atan2(wz, wy);
            }
            if (xzRad != 0) {
                wxzRad += xzRad;
                wx = (float) Math.cos(wxzRad) * wxzDis;
                wz = (float) Math.sin(wxzRad) * wxzDis;
                double y2 = Math.pow(wy, 2);
                wxyDis = (float) (Math.sqrt(Math.pow(wx, 2) + y2));
                wyzDis = (float) (Math.sqrt(y2 + Math.pow(wz, 2)));
                wxyRad = (float) Math.atan2(wy, wx);
                wyzRad = (float) Math.atan2(wz, wy);
            }
            if (yzRad != 0) {
                wyzRad += yzRad;
                wy = (float) Math.cos(wyzRad) * wyzDis;
                wz = (float) Math.sin(wyzRad) * wyzDis;
                double x2 = Math.pow(wx, 2);
                wxyDis = (float) (Math.sqrt(x2 + Math.pow(wy, 2)));
                wxzDis = (float) (Math.sqrt(x2 + Math.pow(wz, 2)));
                wxyRad = (float) Math.atan2(wy, wx);
                wxzRad = (float) Math.atan2(wz, wx);
            }
        }
    }

    private class RelationalLine {

        private final RelationalPoint p1;
        private final RelationalPoint p2;
        private final String id;

        private RelationalLine(float rx1, float ry1, float rz1, float rx2, float ry2, float rz2) {
            this(rx1, ry1, rz1, rx2, ry2, rz2, "");
        }
        private RelationalLine(float rx1, float ry1, float rz1, float rx2, float ry2, float rz2, String id) {
            this.p1 = new RelationalPoint(rx1, ry1, rz1);
            this.p2 = new RelationalPoint(rx2, ry2, rz2);
            this.id = id;
        }

        private void copyTo(Obj obj) {
            obj.addLine(p1.ox, p1.oy, p1.oz, p2.ox, p2.oy, p2.oz);
        }
    }
}
