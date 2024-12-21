package cn.itscloudy.sj3d;

import cn.itscloudy.sj3d.util.AllMouseMotionAdapter;
import cn.itscloudy.sj3d.util.TestBox500;

import javax.swing.*;
import java.awt.*;

public class Rgb2Hsb {

    static World world;
    static int step = 10;

    public static void main(String[] args) {
        TestBox500 box = new TestBox500();

        JFrame boxFrame = box.frame;
        world = new World(boxFrame.getWidth(), boxFrame.getHeight());

        // max saturation(radius) = 100
        // brightness = -y
        Obj cylinder = new Obj(world, -300, 0, 300);
        /*
         * 5 is the step, the smaller the step, the more points, the smoother the cylinder,
         * the slower the rendering and the more memory it consumes
         */
        for (int r = 0; r < 255; r+=step) {
            for (int g = 0; g < 255; g+=step) {
                for (int b = 0; b < 255; b+=step) {
                    float[] hsl = new float[3];
                    Color.RGBtoHSB(r, g, b, hsl);
                    float hueRadian = hsl[0] * 2 * (float) Math.PI;
                    float saturation = hsl[1] * 100;
                    float x = (float) (saturation * Math.cos(hueRadian));
                    float z = (float) (saturation * Math.sin(hueRadian));
                    float y = -hsl[2] * 100;
                    cylinder.addPoint(x, y, z, new Color(r, g, b));
                }
            }
        }

        world.addObj(cylinder);
        box.setNotice("WADS↑↓ move, Ctrl + Mouse rotate. Enter recover");
        box.setContent(world);

        AllMouseMotionAdapter mouseMotionAdapter = new AllMouseMotionAdapter(world);
        boxFrame.addMouseListener(mouseMotionAdapter);
        boxFrame.addMouseMotionListener(mouseMotionAdapter);
        boxFrame.addKeyListener(mouseMotionAdapter);
        boxFrame.setVisible(true);
    }
}
