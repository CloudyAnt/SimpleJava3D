package cn.itscloudy;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Canvas extends JPanel {
    int lenCoefficient = 100;
    int centralX;
    int centralY;

    public Canvas(int canvasLen) {
        this.centralY = this.centralX = (canvasLen - 1) / 2;
    }

    List<PxLint> lines;

    List<Triangle> triangles;

    public void draw(List<Line> lines, List<Triangle> triangles) {
        this.lines = transform(lines);
        this.triangles = triangles;
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D graphics = (Graphics2D) g;
        if (lines != null) {
            for (PxLint line : lines) {
                graphics.drawLine(line.x1, line.y1, line.x2, line.y2);
                if (line.name1 != null) {
                    graphics.drawString(line.name1, line.x1, line.y1);
                } else if (line.name2 != null) {
                    graphics.drawString(line.name2, line.x2, line.y2);
                }
            }
        }
        if (triangles != null) {
            for (Triangle triangle : triangles) {
                List<PxPoint> abPxPoints = triangle.line1.getPxPointsInXRange();
                graphics.setColor(Color.RED);
                for (PxPoint p : abPxPoints) {
                    graphics.drawRect(p.x - 1, p.y - 1, 3, 3);
                }

                List<PxPoint> bcPxPoints = triangle.line2.getPxPointsInXRange();
                graphics.setColor(Color.GREEN);
                for (PxPoint p : bcPxPoints) {
                    graphics.drawRect(p.x - 1, p.y - 1, 3, 3);
                }

                List<PxPoint> acPxPoints = triangle.line3.getPxPointsInXRange();
                graphics.setColor(Color.BLUE);
                for (PxPoint p : acPxPoints) {
                    graphics.drawRect(p.x - 1, p.y - 1, 3, 3);
                }
            }
        }
    }

    public ArrayList<PxLint> transform(List<Line> lines) {
        ArrayList<PxLint> iLines = new ArrayList<>();
        for (Line line : lines) {
            iLines.add(line.toPxLine());
        }
        return iLines;
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