package cn.itscloudy.sj3d;

import cn.itscloudy.sj3d.util.Range;

import java.util.List;

public class PointsScanner {
    private PointsScanner() {
    }

    public static void scanLine(List<FixedColoredPoint3D> container, Point3D a, Point3D b) {
        scanLine(container, a.x, a.y, a.z, b.x, b.y, b.z);
    }
    public static void scanLine(List<FixedColoredPoint3D> container, float x1, float y1, float z1,
                                float x2, float y2, float z2) {
        LineFunc2D xyLineFunc = new LineFunc2D(x1, y1, x2, y2);
        LineFunc2D xzLineFunc = new LineFunc2D(x1, z1, x2, z2);
        LineFunc2D yzLineFunc = new LineFunc2D(y1, z1, y2, z2);
        double minX, maxX;
        if (x1 < x2) {
            minX = x1;
            maxX = x2;
        } else {
            minX = x2;
            maxX = x1;
        }

        scanLine(container, xyLineFunc, xzLineFunc, yzLineFunc, minX, maxX);
    }

    public static void scanLine(List<FixedColoredPoint3D> container, LineFunc2D xyF, LineFunc2D xzF, LineFunc2D yzF,
                                double minXD, double maxXD) {
        int minX = (int) Math.round(minXD);
        int maxX = (int) Math.round(maxXD);
        if (xyF.perpendicular) {
            // line ⊥ X
            Range<Integer> yRange = xyF.dvIRange;
            Integer yMin = yRange.getMin();
            Integer yMax = yRange.getMax();
            if (yzF.perpendicular) {
                // line ⊥ X-Y
                Range<Integer> zRange = yzF.dvIRange;
                float minZ = zRange.getMin();
                float maxZ = zRange.getMax();
                for (float z = minZ; z <= maxZ; z += Quality.current.lineWidth) {
                    container.add(new FixedColoredPoint3D((float) minXD, yMin, z));
                }
            } else {
                for (float y = yMin; y <= yMax; y += Quality.current.lineWidth) {
                    float z = yzF.getDvByIv(y);
                    container.add(new FixedColoredPoint3D(minX, y, z));
                }
            }
        } else {
            for (float x = minX; x <= maxX; x += Quality.current.lineWidth) {
                float y = xyF.getDvByIv(x);
                float z = xzF.getDvByIv(x);
                container.add(new FixedColoredPoint3D(x, y, z));
            }
        }
    }

    public static void scanTriangleByX(List<FixedColoredPoint3D> container, Point3D a, Point3D b, Point3D c) {
        Point3D p1, p2, p3;
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
                Point3D temp = p1;
                p1 = p2;
                p2 = temp;
            }
        }

        FixedLine3D l1 = new FixedLine3D(p1, p2);
        FixedLine3D l2 = new FixedLine3D(p2, p3);
        FixedLine3D l3 = new FixedLine3D(p1, p3);

        // p1.x to p2.x
        scanTriangleByX(container, p1.x, p2.x, l1, l3);
        // p2.x to p3.x
        scanTriangleByX(container, p2.x, p3.x, l2, l3);
    }

    // from = mainLine.x, to = coLine.x
    private static void scanTriangleByX(List<FixedColoredPoint3D> container, double fromD, double toD,
                         FixedLine3D mainLine, FixedLine3D coLine) {
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
                    PointsScanner.scanLine(container, from, y, z, x1, y, z1);
                }
            } else {
                for (float y = minY; y <= maxY; y += Quality.current.lineWidth) {
                    float z = mainLine.yzLineFunc.getDvByIv(y);
                    float z1 = coLine.yzLineFunc.getDvByIv(y);
                    Float x1 = coLine.xyLineFunc.getIvByDv(y);
                    if (x1 == null) {
                        continue;
                    }
                    PointsScanner.scanLine(container, from, y, z, x1, y, z1);
                }
            }

        } else {
            for (float x = from; x <= to; x += Quality.current.lineWidth) {
                float y = mainLine.xyLineFunc.getDvByIv(x);
                float z = mainLine.xzLineFunc.getDvByIv(x);

                float y1 = coLine.xyLineFunc.getDvByIv(x);
                float z1 = coLine.xzLineFunc.getDvByIv(x);
                PointsScanner.scanLine(container, x, y, z, x, y1, z1);
            }
        }
    }

    public static void scanTriangleByY(List<FixedColoredPoint3D> container, Point3D a, Point3D b, Point3D c) {
        Point3D p1, p2, p3;
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
                Point3D temp = p1;
                p1 = p2;
                p2 = temp;
            }
        }

        FixedLine3D l1 = new FixedLine3D(p1, p2);
        FixedLine3D l2 = new FixedLine3D(p2, p3);
        FixedLine3D l3 = new FixedLine3D(p1, p3);

        // p1.y to p2.y
        scanTriangleByY(container, p1.y, p2.y, l1, l3);
        // p2.y to p3.y
        scanTriangleByY(container, p2.y, p3.y, l2, l3);
    }

    private static void scanTriangleByY(List<FixedColoredPoint3D> container, double fromD, double toD,
                         FixedLine3D mainLine, FixedLine3D coLine) { // from = mainLine.x, to = coLine.x
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
                    PointsScanner.scanLine(container, x, from, z, x1, y1, z);
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
                    PointsScanner.scanLine(container, x, from, z, x1, y1, z);
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
                PointsScanner.scanLine(container, x, y, z, x1, y, z1);
            }
        }
    }
}
