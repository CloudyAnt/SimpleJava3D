package cn.itscloudy.sj3d;

import cn.itscloudy.sj3d.util.AllMouseMotionAdapter;
import cn.itscloudy.sj3d.util.TestBox500;

import javax.swing.*;

public class WorldTest {

    static World world;

    public static void main(String[] args) {
        TestBox500 box = new TestBox500();

        JFrame boxFrame = box.frame;
        world = new World(boxFrame.getWidth(), boxFrame.getHeight());

        Obj obj = new Obj(world, -300, 0, 300);
        obj.addLine(100, 100, -100, 100, 100, 100, "A");
        obj.addLine(-100, 100, -100,-100, 100, 100, "B");
        obj.addLine(-100, -100, -100,-100, -100, 100, "C");
        obj.addLine(100, -100, -100,100, -100, 100, "D");

        obj.addLine(100, 100, -100, 100, -100, -100, "α");
        obj.addLine(-100, 100, -100,-100, -100, -100, "β");
        obj.addLine(100, 100, 100,100, -100, 100, "γ");
        obj.addLine(-100, 100, 100,-100, -100, 100, "δ");

        obj.addLine(100, 100, -100, -100, 100, -100, "1");
        obj.addLine(100, -100, -100,-100, -100, -100, "2");
        obj.addLine(100, 100, 100,-100, 100, 100, "3");
        obj.addLine(100, -100, 100,-100, -100, 100, "4");

        obj.addTriangle(-100, 100, -100, 100, 100, 100, 100, -100, 100);

        Obj obj1 = obj.copy(300, 0, 600);
        world.addObj(obj);
        world.addObj(obj1);

        box.setNotice("WADS↑↓ move, Ctrl + Mouse rotate. Enter recover");
        box.setContent(world);

        AllMouseMotionAdapter mouseMotionAdapter = new AllMouseMotionAdapter(world);
        boxFrame.addMouseListener(mouseMotionAdapter);
        boxFrame.addMouseMotionListener(mouseMotionAdapter);
        boxFrame.addKeyListener(mouseMotionAdapter);
        boxFrame.setVisible(true);

    }

}
