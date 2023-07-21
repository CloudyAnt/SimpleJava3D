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

    public World(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        this.centralX = Math.round((float) width / 2);
        this.centralY = Math.round((float) height / 2);
        objs = new ArrayList<>();
    }

    public Point to2D(double x, double y, double z) {
        if (z <= 50) {
            return null;
        }
        return new Point((int) ((x / z) * LEN_COEFFICIENT + centralX), (int) ((y / z) * LEN_COEFFICIENT + centralY));
    }

    public void addObj(Obj obj) {
        objs.add(obj);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D graphics2D = (Graphics2D) g;
        objs.forEach(o -> o.draw(this, graphics2D));
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
        repaint();
    }

}
