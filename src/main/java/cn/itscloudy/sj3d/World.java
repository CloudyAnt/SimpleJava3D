package cn.itscloudy.sj3d;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class World extends JPanel {
    public static final int LEN_COEFFICIENT = 100;

    private final int centralX;
    private final int centralY;
    private final List<Obj> objs;
    private int xOffs;
    private int yOffs;
    private int zOffs;

    public World(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        this.centralX = Math.round((float) width / 2);
        this.centralY = Math.round((float) height / 2);
        objs = new ArrayList<>();
    }

    public Point to2D(double x, double y, double z) {
        if (z < 1) {
            return null;
        }
        return new Point((int) ((x / z) * LEN_COEFFICIENT + centralX + xOffs),
                (int) ((y / z) * LEN_COEFFICIENT + centralY + yOffs));
    }

    public void addObj(Obj obj) {
        objs.add(obj);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D graphics2D = (Graphics2D) g;
        objs.forEach(o -> o.draw(graphics2D));
    }

    public void rotate(RotationDirection direction, int degree) {
        switch (direction) {
            case UP_DOWN:
                objs.forEach(o -> o.rotateYz(degree));
                break;
            case LEFT_RIGHT:
                objs.forEach(o -> o.rotateXz(degree));
                break;
            case CLOCK:
                objs.forEach(o -> o.rotateXy(degree));
                break;
            default:
                return;
        }
        repaint();
    }

    public void restore() {
        objs.forEach(Obj::restore);
        xOffs = yOffs = zOffs = 0;
        repaint();
    }

    public void addZ(int offs) {
        zOffs += offs;
        resetRadAndDis(0, 0, offs);
    }

    public void addX(int offs) {
        xOffs += offs;
        resetRadAndDis(offs, 0, 0);
    }

    public void addY(int offs) {
        yOffs += offs;
        resetRadAndDis(0, offs, 0);
    }

    public void resetRadAndDis(int xOffs, int yOffs, int zOffs) {
        objs.forEach(o -> o.resetRadAndDis(xOffs, yOffs, zOffs));
        repaint();
    }
}
