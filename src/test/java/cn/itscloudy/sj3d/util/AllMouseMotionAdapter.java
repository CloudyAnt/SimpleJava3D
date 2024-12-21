package cn.itscloudy.sj3d.util;

import cn.itscloudy.sj3d.RotationDirection;
import cn.itscloudy.sj3d.World;

import java.awt.*;
import java.awt.event.*;

public class AllMouseMotionAdapter implements MouseMotionListener, MouseListener, KeyListener {

    private int x;
    private int y;
    private int sx;
    private final World world;
    public AllMouseMotionAdapter(World world) {
        this.world = world;
    }

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
//            if (e.isControlDown()) {
//                return;
//            }
//
//            int dx = e.getX();
//            int dy = e.getY();
//
//            int xOffs = dx - x;
//            if (xOffs > 10) {
//                x = dx;
//                world.rotate(RotationDirection.LEFT_RIGHT, 10);
//            } else if (xOffs < -10) {
//                x = dx;
//                world.rotate(RotationDirection.LEFT_RIGHT, -10);
//            }
//
//            int yOffs = dy - y;
//            if (yOffs > 10) {
//                y = dy;
//                world.rotate(RotationDirection.UP_DOWN, 10);
//            } else if (yOffs < -10) {
//                y = dy;
//                world.rotate(RotationDirection.UP_DOWN, -10);
//            }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (e.isControlDown()) {
            int dx = e.getX();

            int xOffs = dx - sx;
            if (xOffs > 5) {
                sx = dx;
                world.rotate(RotationDirection.LEFT_RIGHT, 2);
            } else if (xOffs < -2) {
                sx = dx;
                world.rotate(RotationDirection.LEFT_RIGHT, -2);
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
            world.addZ(-5);
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            world.addZ(5);
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            world.addX(5);
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            world.addX(-5);
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            world.addY(5);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            world.addY(-5);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
