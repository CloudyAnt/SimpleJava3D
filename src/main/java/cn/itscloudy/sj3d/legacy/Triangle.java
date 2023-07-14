package cn.itscloudy.sj3d.legacy;

import cn.itscloudy.sj3d.util.Range;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class Triangle {

    private final SatellitePoint c;
    private final SatellitePoint b;
    private final SatellitePoint a;

    private static final int B_EQUAL_A = 1;
    private static final int C_EQUAL_A = 2;
    private static final int ALL_EQUAL = B_EQUAL_A | C_EQUAL_A;

    private final int OVERLAPPING;

    List<SatellitePoint> points;

    Color color;

    Triangle(SatellitePoint a, SatellitePoint b, SatellitePoint c, Color color) {
        int overlapping = 0;
        if (b.equals(a)) {
            overlapping |= B_EQUAL_A;
        }
        if (c.equals(a)) {
            overlapping |= C_EQUAL_A;
        }
        this.OVERLAPPING = overlapping;

        this.c = c;
        this.b = b;
        this.a = a;
        this.color = color;
        points = new ArrayList<>();
        if (OVERLAPPING == ALL_EQUAL) {
            points.add(a);
        }
        reset();
    }

    void reset() {
        if (OVERLAPPING == ALL_EQUAL) {
            return;
        }
        points.clear();
        if (OVERLAPPING == B_EQUAL_A) {
            Line.scanLinePoints(points, a, c);
        } else if (OVERLAPPING == C_EQUAL_A) {
            Line.scanLinePoints(points, a, b);
        } else if (Math.round(a.x) != Math.round(b.x) || Math.round(a.x) != Math.round(c.x)) {
            // it's not parallel to X surface
            scanByX();
        } else if (Math.round(a.y) != Math.round(b.y) || Math.round(a.y) != Math.round(c.y)) {
            // it's not parallel to Y surface
            scanByY();
        } else {
            // at this moment, the three points are all in a line which parallel to Z surface
            SatellitePoint p1, p2, p3;
            if (b.z > a.z) {
                p1 = a;
                p2 = b;
            } else {
                p1 = b;
                p2 = a;
            }
            if (c.z > p2.z) {
                p3 = c;
            } else {
                p3 = p2;
            }
            Line.scanLinePoints(points, p1, p3);
        }


        points.forEach(p -> p.color = color);
    }

    private void scanByZ() {
        SatellitePoint p1, p2, p3;
        if (b.z > a.z) {
            p1 = a;
            p2 = b;
        } else {
            p1 = b;
            p2 = a;
        }
        if (c.z > p2.z) {
            p3 = c;
        } else {
            p3 = p2;
            p2 = c;
            if (p1.z > p2.z) {
                SatellitePoint temp = p1;
                p1 = p2;
                p2 = temp;
            }
        }

        Line l1 = new Line(p1, p2);
        Line l2 = new Line(p2, p3);
        Line l3 = new Line(p1, p3);

        // p1.z to p2.z
        scanByZ(p1.z, p2.z, l1, l3);
        // p2.z to p3.z
        scanByZ(p2.z, p3.z, l2, l3);

    }

    private void scanByY() {
        SatellitePoint p1, p2, p3;
        if (b.y > a.y) {
            p1 = a;
            p2 = b;
        } else {
            p1 = b;
            p2 = a;
        }
        if (c.y > p2.y) {
            p3 = c;
        } else {
            p3 = p2;
            p2 = c;
            if (p1.y > p2.y) {
                SatellitePoint temp = p1;
                p1 = p2;
                p2 = temp;
            }
        }

        Line l1 = new Line(p1, p2);
        Line l2 = new Line(p2, p3);
        Line l3 = new Line(p1, p3);

        // p1.y to p2.y
        scanByY(p1.y, p2.y, l1, l3);
        // p2.y to p3.y
        scanByY(p2.y, p3.y, l2, l3);
    }

    private void scanByX() {
        SatellitePoint p1, p2, p3;
        if (b.x > a.x) {
            p1 = a;
            p2 = b;
        } else {
            p1 = b;
            p2 = a;
        }
        if (c.x > p2.x) {
            p3 = c;
        } else {
            p3 = p2;
            p2 = c;
            if (p1.x > p2.x) {
                SatellitePoint temp = p1;
                p1 = p2;
                p2 = temp;
            }
        }

        Line l1 = new Line(p1, p2);
        Line l2 = new Line(p2, p3);
        Line l3 = new Line(p1, p3);

        // p1.x to p2.x
        scanByX(p1.x, p2.x, l1, l3);
        // p2.x to p3.x
        scanByX(p2.x, p3.x, l2, l3);
    }

    void scanByX(double fromD, double toD, Line mainLine, Line coLine) { // from = mainLine.x, to = coLine.x
        // x - y - z
        int from = (int) Math.round(fromD);
        int to = (int) Math.round(toD);
        if (mainLine.xPerp) {
            if (mainLine.yPerp || coLine.yPerp) {
                // the only line can be covered by the 2nd scan
                return;
            }

            Range<Integer> yRange = mainLine.xyLineFunc.dvIRange;
            Integer minY = yRange.getMin();
            Integer maxY = yRange.getMax();
            if (mainLine.zPerp) {
                // mainLine perpendicular to X-Z surface
                Integer z = mainLine.xzLineFunc.dvIRange.getMin();
                for (float y = minY; y <= maxY; y += Quality.current.lineWidth) {
                    float z1 = coLine.yzLineFunc.getDvByIv(y);
                    Float x1;
                    if (coLine.xPerp) {
                        // x fixed when perpendicular to x
                        x1 = (float) from;
                    } else {
                        // get x by y
                        x1 = coLine.xyLineFunc.getIvByDv(y);
                        if (x1 == null) {
                            continue;
                        }
                    }
                    Line.scanLinePoints(points, from, y, z, x1, y, z1);
                }
            } else {
                for (float y = minY; y <= maxY; y += Quality.current.lineWidth) {
                    float z = mainLine.yzLineFunc.getDvByIv(y);
                    float z1 = coLine.yzLineFunc.getDvByIv(y);
                    Float x1 = coLine.xyLineFunc.getIvByDv(y);
                    if (x1 == null) {
                        continue;
                    }
                    Line.scanLinePoints(points, from, y, z, x1, y, z1);
                }
            }

        } else {
            for (float x = from; x <= to; x += Quality.current.lineWidth) {
                float y = mainLine.xyLineFunc.getDvByIv(x);
                float z = mainLine.xzLineFunc.getDvByIv(x);

                float y1 = coLine.xyLineFunc.getDvByIv(x);
                float z1 = coLine.xzLineFunc.getDvByIv(x);
                Line.scanLinePoints(points, x, y, z, x, y1, z1);
            }
        }
    }

    void scanByY(double fromD, double toD, Line mainLine, Line coLine) { // from = mainLine.x, to = coLine.x
        // y - z - x
        int from = (int) Math.round(fromD);
        int to = (int) Math.round(toD);
        if (mainLine.yPerp) {

            if (mainLine.zPerp || coLine.zPerp) {
                // the only line can be covered by the 2nd scan
                return;
            }

            Range<Integer> zRange = mainLine.yzLineFunc.dvIRange;
            Integer minZ = zRange.getMin();
            Integer maxZ = zRange.getMax();

            if (mainLine.xPerp) {
                // mainLine perpendicular to Y-X surface
                Integer x = mainLine.xyLineFunc.ivIRange.getMin();
                for (float z = minZ; z <= maxZ; z += Quality.current.lineWidth) {
                    Float x1 = coLine.xzLineFunc.getIvByDv(z);
                    if (x1 == null) {
                        continue;
                    }

                    Float y1;
                    if (coLine.yPerp) {
                        // y fixed when perp to y
                        y1 = (float) from;
                    } else {
                        // get y by z
                        y1 = coLine.yzLineFunc.getIvByDv(z);
                    }
                    Line.scanLinePoints(points, x, from, z, x1, y1, z);
                }
            } else {
                for (float z = minZ; z <= maxZ; z += Quality.current.lineWidth) {
                    Float x = mainLine.xzLineFunc.getIvByDv(z);
                    if (x == null) {
                        continue;
                    }
                    Float x1 = coLine.xzLineFunc.getIvByDv(z);
                    if (x1 == null) {
                        continue;
                    }
                    Float y1 = coLine.yzLineFunc.getIvByDv(z);
                    if (y1 == null) {
                        continue;
                    }
                    Line.scanLinePoints(points, x, from, z, x1, y1, z);
                }
            }

        } else {
            for (float y = from; y <= to; y += Quality.current.lineWidth) {
                float z = mainLine.yzLineFunc.getDvByIv(y);
                Float x = mainLine.xyLineFunc.getIvByDv(y);
                if (x == null) {
                    continue;
                }

                float z1 = coLine.yzLineFunc.getDvByIv(y);
                Float x1 = coLine.xyLineFunc.getIvByDv(y);
                if (x1 == null) {
                    continue;
                }
                Line.scanLinePoints(points, x, y, z, x1, y, z1);
            }
        }
    }


    private void scanByZ(double fromD, double toD, Line mainLine, Line coLine) {
        // z - x - y
        int from = (int) Math.round(fromD);
        int to = (int) Math.round(toD);
        if (mainLine.zPerp) {
            if (mainLine.xPerp || coLine.xPerp) {
                // the only line can be covered by the 2nd scan
                return;
            }

            Range<Integer> xRange = mainLine.xzLineFunc.ivIRange;
            Integer minX = xRange.getMin();
            Integer maxX = xRange.getMax();
            if (mainLine.yPerp) {
                // mainLine perpendicular to Y-Z surface
                Integer y = mainLine.yzLineFunc.ivIRange.getMin();
                for (float x = minX; x <= maxX; x += Quality.current.lineWidth) {
                    float y1 = coLine.xyLineFunc.getDvByIv(x);

                    float z1;
                    if (coLine.zPerp) {
                        z1 = from;
                    } else {
                        z1 = coLine.xzLineFunc.getDvByIv(x);
                    }
                    Line.scanLinePoints(points, x, y, from, x, y1, z1);
                }
            } else {
                for (float x = minX; x <= maxX; x += Quality.current.lineWidth) {
                    float y = mainLine.xyLineFunc.getDvByIv(x);
                    float y1 = coLine.xyLineFunc.getDvByIv(x);
                    float z1 = coLine.xzLineFunc.getDvByIv(x);
                    Line.scanLinePoints(points, x, y, from, x, y1, z1);
                }
            }

        } else {
            for (float z = from; z <= to; z += Quality.current.lineWidth) {
                Float x = mainLine.xzLineFunc.getIvByDv(z);
                if (x == null) {
                    continue;
                }
                Float y = mainLine.yzLineFunc.getIvByDv(z);
                if (y == null) {
                    continue;
                }
                Float x1 = coLine.xzLineFunc.getIvByDv(z);
                if (x1 == null) {
                    continue;
                }
                Float y1 = coLine.yzLineFunc.getIvByDv(z);
                if (y1 == null) {
                    continue;
                }
                Line.scanLinePoints(points, x, y, z, x1, y1, z);
            }
        }
    }
}
