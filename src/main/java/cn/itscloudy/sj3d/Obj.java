package cn.itscloudy.sj3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Obj extends Point3D {

    private final float originalX;
    private final float originalY;
    private final float originalZ;
    private final float originalXzDis;
    private final float originalXzRad;
    private final World world;
    private final List<RelationalPoint> points;
    private final List<RelationalLine> lines;
    private final List<RelationalTriangle> triangles;
    private float xyRadOffs; // Z rotation
    private float xzRadOffs; // Y rotation
    private float yzRadOffs; // X rotation
    private float xzRad;
    private float xzDis;

    public Obj(World world, float x, float y, float z) {
        super(x, y, z);
        this.world = world;
        originalX = x;
        originalY = y;
        originalZ = z;
        originalXzDis = (float) (Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2)));
        originalXzRad = (float) Math.atan2(z, x);
        xzRad = originalXzRad;
        xzDis = originalXzDis;
        points = new ArrayList<>();
        lines = new ArrayList<>();
        triangles = new ArrayList<>();
    }

    /**
     * @param rx relative x
     * @param ry relative y
     * @param rz relative z
     */
    public void addPoint(float rx, float ry, float rz) {
        points.add(new RelationalPoint(rx, ry, rz));
    }

    public void addPoint(float x, float y, float z, Color color) {
        RelationalPoint p = new RelationalPoint(x, y, z);
        p.color = color;
        points.add(p);
    }

    public void addLine(float rx1, float ry1, float rz1, float rx2, float ry2, float rz2, String id) {
        lines.add(new RelationalLine(rx1, ry1, rz1, rx2, ry2, rz2, id));
    }

    public void addLine(float rx1, float ry1, float rz1, float rx2, float ry2, float rz2) {
        lines.add(new RelationalLine(rx1, ry1, rz1, rx2, ry2, rz2));
    }

    public void addTriangle(float rx1, float ry1, float rz1, float rx2, float ry2, float rz2
            , float rx3, float ry3, float rz3) {
        triangles.add(new RelationalTriangle(rx1, ry1, rz1, rx2, ry2, rz2, rx3, ry3, rz3));
    }

    public void draw(Graphics2D graphics2D) {
        for (RelationalPoint rp : points) {
            Point point = getPoint2D(rp);
            if (point == null) return;
            graphics2D.setColor(rp.color);
            graphics2D.drawString(String.valueOf(rp.id), point.x, point.y);
        }
        for (RelationalLine line : lines) {
            Point p1 = getPoint2D(line.p1);
            Point p2 = getPoint2D(line.p2);
            if (p1 == null || p2 == null) {
                return;
            }
            graphics2D.setColor(line.color);
            graphics2D.drawString(line.id, (p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
            graphics2D.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
        List<FixedColoredPoint3D> trianglePoints = new ArrayList<>();
        for (RelationalTriangle triangle : triangles) {
            trianglePoints.addAll(triangle.getContainedPoints());
        }
        trianglePoints.sort((a, b) -> Float.compare(b.z, a.z));
        for (FixedColoredPoint3D tp : trianglePoints) {
            graphics2D.setColor(tp.color);
            Point point = getPoint2D(tp.x, tp.y, tp.z);
            int x = point.x - Quality.current.paintingOffset;
            int y = point.y - Quality.current.paintingOffset;
            graphics2D.drawRect(x, y, Quality.current.rectLength, Quality.current.rectLength);
        }
    }

    private Point getPoint2D(RelationalPoint rp) {
        float x = this.x + rp.x;
        float y = this.y + rp.y;
        float z = this.z + rp.z;
        return world.to2D(x, y, z);
    }

    private Point getPoint2D(float x, float y, float z) {
        x += this.x;
        y += this.y;
        z += this.z;
        return world.to2D(x, y, z);
    }

    public void rotateXy(int degree) {
        xyRadOffs += Math.toRadians(degree);
        updateAll();
    }

    public void rotateXz(int degree) {
        xzRadOffs += Math.toRadians(degree);
        float rad = xzRad + xzRadOffs;
        x = (float) Math.cos(rad) * xzDis;
        z = (float) Math.sin(rad) * xzDis;
        updateAll();
    }

    public void rotateYz(int degree) {
        yzRadOffs += Math.toRadians(degree);
        updateAll();
    }

    private void updateAll() {
        triangles.forEach(t -> {
            t.p1.update();
            t.p2.update();
            t.p3.update();
        });
        lines.forEach(l -> {
            l.p1.update();
            l.p2.update();
        });
        points.forEach(RelationalPoint::update);
    }

    public void restore() {
        x = originalX;
        y = originalY;
        z = originalZ;
        xzRad = originalXzRad;
        xzDis = originalXzDis;
        xyRadOffs = 0;
        xzRadOffs = 0;
        yzRadOffs = 0;
        updateAll();
    }

    public Obj copy(float x, float y, float z) {
        Obj o = new Obj(world, x, y, z);
        triangles.forEach(t -> t.copyTo(o));
        points.forEach(p -> p.copyTo(o));
        lines.forEach(l -> l.copyTo(o));
        return o;
    }

    public void resetRadAndDis(int xOffs, int yOffs, int zOffs) {
        x += xOffs;
        y += yOffs;
        z += zOffs;
        xzDis = (float) (Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2)));
        xzRad = (float) Math.atan2(z, x);
    }

    private class RelationalPoint extends Point3D {
        private final String id;
        // o prefixed variables coordinates in perspective of Obj
        private final float ox;
        private final float oy;
        private final float oz;
        private final float oxyDis; // xy projection distance
        private final float oxzDis; // xz projection distance
        private final float oyzDis; // yz projection distance
        private final float oxyRad;
        private final float oyzRad;
        private final float oxzRad;
        // w prefixed variables is coordinates in perspective of World (Obj as the origin)
        private float wxyDis; // xy projection distance
        private float wxzDis; // xz projection distance
        private float wyzDis; // yz projection distance
        private float wxyRad;
        private float wxzRad;
        private float wyzRad;
        private Color color = Color.BLACK;

        private RelationalPoint(float ox, float oy, float oz) {
            this(ox, oy, oz, ".");
        }

        private RelationalPoint(float ox, float oy, float oz, String id) {
            super(ox, oy, oz);
            this.id = id;
            this.ox = ox;
            this.oy = oy;
            this.oz = oz;

            double x2 = Math.pow(x, 2);
            double y2 = Math.pow(y, 2);
            oxyDis = (float) (Math.sqrt(x2 + y2));
            double z2 = Math.pow(z, 2);
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
            x = ox;
            y = oy;
            z = oz;
            wxyDis = oxyDis;
            wxzDis = oxzDis;
            wyzDis = oyzDis;
            wxyRad = oxyRad;
            wxzRad = oxzRad;
            wyzRad = oyzRad;
        }

        private void update() {
            restore();
            if (xyRadOffs != 0) {
                wxyRad += xyRadOffs;
                x = (float) Math.cos(wxyRad) * wxyDis;
                y = (float) Math.sin(wxyRad) * wxyDis;
                double z2 = Math.pow(z, 2);
                wxzDis = (float) (Math.sqrt(Math.pow(x, 2) + z2));
                wyzDis = (float) (Math.sqrt(Math.pow(y, 2) + z2));
                wxzRad = (float) Math.atan2(z, x);
                wyzRad = (float) Math.atan2(z, y);
            }
            if (xzRadOffs != 0) {
                wxzRad += xzRadOffs;
                x = (float) Math.cos(wxzRad) * wxzDis;
                z = (float) Math.sin(wxzRad) * wxzDis;
                double y2 = Math.pow(y, 2);
                wxyDis = (float) (Math.sqrt(Math.pow(x, 2) + y2));
                wyzDis = (float) (Math.sqrt(y2 + Math.pow(z, 2)));
                wxyRad = (float) Math.atan2(y, x);
                wyzRad = (float) Math.atan2(z, y);
            }
            if (yzRadOffs != 0) {
                wyzRad += yzRadOffs;
                y = (float) Math.cos(wyzRad) * wyzDis;
                z = (float) Math.sin(wyzRad) * wyzDis;
                double x2 = Math.pow(x, 2);
                wxyDis = (float) (Math.sqrt(x2 + Math.pow(y, 2)));
                wxzDis = (float) (Math.sqrt(x2 + Math.pow(z, 2)));
                wxyRad = (float) Math.atan2(y, x);
                wxzRad = (float) Math.atan2(z, x);
            }
        }
    }

    private class RelationalLine {

        private final RelationalPoint p1;
        private final RelationalPoint p2;
        private final String id;
        private Color color = Color.BLACK;

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

    private class RelationalTriangle {
        private static final int B_EQUAL_A = 1;
        private static final int C_EQUAL_A = 2;
        private static final int ALL_EQUAL = B_EQUAL_A | C_EQUAL_A;
        private final int OVERLAPPING;

        private final RelationalPoint p1;
        private final RelationalPoint p2;
        private final RelationalPoint p3;

        private final List<FixedColoredPoint3D> containedPoints;

        private RelationalTriangle(float rx1, float ry1, float rz1, float rx2, float ry2, float rz2
                , float rx3, float ry3, float rz3) {
            this(new RelationalPoint(rx1, ry1, rz1), new RelationalPoint(rx2, ry2, rz2),
                    new RelationalPoint(rx3, ry3, rz3));
        }

        private RelationalTriangle(RelationalPoint p1, RelationalPoint p2, RelationalPoint p3) {
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
            int overlapping = 0;
            if (p1.x == p2.x && p1.y == p2.y && p1.z == p2.z) {
                overlapping |= B_EQUAL_A;
            }
            if (p1.x == p3.x && p1.y == p3.y && p1.z == p3.z) {
                overlapping |= C_EQUAL_A;
            }
            this.OVERLAPPING = overlapping;
            containedPoints = new ArrayList<>();
            if (OVERLAPPING == ALL_EQUAL) {
                containedPoints.add(new FixedColoredPoint3D(p1));
            }
        }

        private List<FixedColoredPoint3D> getContainedPoints() {
            if (OVERLAPPING == ALL_EQUAL) {
                return containedPoints;
            }
            containedPoints.clear();
            if (OVERLAPPING == B_EQUAL_A) {
                PointsScanner.scanLine(containedPoints, p1, p3);
            } else if (OVERLAPPING == C_EQUAL_A) {
                PointsScanner.scanLine(containedPoints, p1, p2);
            } else if (Math.round(p1.x) != Math.round(p2.x) || Math.round(p1.x) != Math.round(p3.x)) {
                // not parallel to X
                PointsScanner.scanTriangleByX(containedPoints, p1, p2, p3);
            } else if (Math.round(p1.y) != Math.round(p2.y) || Math.round(p1.y) != Math.round(p3.y)) {
                // not parallel to Y
                PointsScanner.scanTriangleByY(containedPoints, p1, p2, p3);
            } else {
                // a line parallel to Z
                Point3D a, b, c;
                if (p2.z > p1.z) {
                    a = p1;
                    b = p2;
                } else {
                    a = p2;
                    b = p1;
                }
                if (p3.z > b.z) {
                    c = p3;
                } else {
                    c = b;
                    b = p3;
                    if (a.z > b.z) {
                        a = b;
                    }
                }
                PointsScanner.scanLine(containedPoints, a, c);
            }
            return containedPoints;
        }

        public void copyTo(Obj o) {
            o.addTriangle(p1.ox, p1.oy, p1.oz, p2.ox, p2.oy, p2.oz, p3.ox, p3.oy, p3.oz);
        }
    }
}
