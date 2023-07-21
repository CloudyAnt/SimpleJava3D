package cn.itscloudy.sj3d;

import cn.itscloudy.sj3d.util.TestBox500;

import java.awt.*;
import java.awt.event.*;

public class ObjRotationTest {

    static World world;

    public static void main(String[] args) {
        TestBox500 box = new TestBox500();
        world = new World(box.getWidth(), box.getHeight());

        Obj obj = new Obj(-300, 0, 300);
        obj.addLine(100, 100, -100, 100, 100, 100, "A");
        obj.addLine(-100, 100, -100,-100, 100, 100, "B");
        obj.addLine(-100, -100, -100,-100, -100, 100, "C");
        obj.addLine(100, -100, -100,100, -100, 100, "D");

        Obj obj1 = obj.copy(300, 0, 300);
        world.addObj(obj);
        world.addObj(obj1);

        box.add(world);

        AllMouseMotionAdapter mouseMotionAdapter = new AllMouseMotionAdapter();
        box.addMouseListener(mouseMotionAdapter);
        box.addMouseMotionListener(mouseMotionAdapter);
        box.addKeyListener(mouseMotionAdapter);
        box.setVisible(true);

    }

    private static class AllMouseMotionAdapter implements MouseMotionListener, MouseListener, KeyListener {

        private int x;
        private int y;
        private int sx;

        @Override
        public void mouseClicked(MouseEvent e) {
            x = e.getX();
            y = e.getY();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (e.isControlDown()) {
                return;
            }

            int dx = e.getX();
            int dy = e.getY();

            int xOffs = dx - x;
            if (xOffs > 10) {
                x = dx;
                world.rotate(RotationDirection.LEFT_RIGHT, 10);
            } else if (xOffs < -10) {
                x = dx;
                world.rotate(RotationDirection.LEFT_RIGHT, -10);
            }

            int yOffs = dy - y;
            if (yOffs > 10) {
                y = dy;
                world.rotate(RotationDirection.UP_DOWN, 10);
            } else if (yOffs < -10) {
                y = dy;
                world.rotate(RotationDirection.UP_DOWN, -10);
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (e.isControlDown()) {
                int dx = e.getX();

                int xOffs = dx - sx;
                if (xOffs > 10) {
                    sx = dx;
                    world.rotate(RotationDirection.CLOCK, 10);
                } else if (xOffs < -10) {
                    sx = dx;
                    world.rotate(RotationDirection.CLOCK, -10);
                }
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
                sx = mouseLoc.x;
            } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                world.restore();
            } else if (e.getKeyCode() == KeyEvent.VK_W) {
                world.rotate(RotationDirection.UP_DOWN, -10);
            } else if (e.getKeyCode() == KeyEvent.VK_S) {
                world.rotate(RotationDirection.UP_DOWN, 10);
            } else if (e.getKeyCode() == KeyEvent.VK_A) {
                world.rotate(RotationDirection.LEFT_RIGHT, -10);
            } else if (e.getKeyCode() == KeyEvent.VK_D) {
                world.rotate(RotationDirection.LEFT_RIGHT, 10);
            } else if (e.getKeyCode() == KeyEvent.VK_Q) {
                world.rotate(RotationDirection.CLOCK, -10);
            } else if (e.getKeyCode() == KeyEvent.VK_E) {
                world.rotate(RotationDirection.CLOCK, 10);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}
