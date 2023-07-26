package cn.itscloudy.sj3d.legacy;

import cn.itscloudy.sj3d.FixedColoredPoint3D;
import cn.itscloudy.sj3d.FixedPoint3D;
import cn.itscloudy.sj3d.Quality;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Canvas extends JPanel {
    public static Canvas instance;
    static int closeSurfaceDis = 500;
    static int farSurfaceDis = 1100;
    static FixedPoint3D center = new FixedPoint3D(0, 0,
            (float) (closeSurfaceDis + farSurfaceDis) / 2);

    int lenCoefficient = 100;
    int centralX;
    int centralY;

    List<PxLine> pxLines;

    List<Triangle> triangles;

    public Canvas(int canvasLen) {
        this.pxLines = new ArrayList<>();
        this.triangles = new ArrayList<>();
        this.centralY = this.centralX = (canvasLen - 1) / 2;
    }

    public void draw(List<Line> lines, List<Triangle> triangles) {
        transform(lines);
        this.triangles.clear();
        this.triangles.addAll(triangles);
        this.repaint();
    }

    public void draw(List<Line> lines) {
        transform(lines);
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D graphics = (Graphics2D) g;
        for (PxLine line : pxLines) {
            graphics.drawLine(line.x1, line.y1, line.x2, line.y2);
            if (line.name1 != null) {
                graphics.drawString(line.name1, line.x1, line.y1);
            } else if (line.name2 != null) {
                graphics.drawString(line.name2, line.x2, line.y2);
            }
        }

        List<FixedColoredPoint3D> trianglePoints = new ArrayList<>();
        for (Triangle triangle : triangles) {
            trianglePoints.addAll(triangle.points);
        }
        trianglePoints.sort((a, b) -> Float.compare(b.z, a.z));
        for (FixedColoredPoint3D tp : trianglePoints) {
            graphics.setColor(tp.color);
            int x = Canvas.instance.toPxX(tp.x, tp.z) - Quality.current.paintingOffset;
            int y = Canvas.instance.toPxY(tp.y, tp.z) - Quality.current.paintingOffset;
            graphics.drawRect(x, y, Quality.current.rectLength, Quality.current.rectLength);
        }
    }

    public void transform(List<Line> lines) {
        pxLines.clear();
        for (Line line : lines) {
            pxLines.add(line.toPxLine());
        }
    }

    int toPxX(double x, double z) {
        // 1. inverse ratio base on the surface z = 100
        // 2. multiply lenCoefficient
        // 3. align to center
        return (int) ((x / (z + 100)) * lenCoefficient + centralX);
    }

    int toPxY(double y, double z) {
        // similar to toPxX()
        return (int) ((y / (z + 100)) * lenCoefficient + centralY);
    }
}