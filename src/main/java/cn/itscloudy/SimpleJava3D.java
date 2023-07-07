package cn.itscloudy;

import cn.itscloudy.util.Dragger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Consumer;

public class SimpleJava3D {
    static int canvasLen = 601;
    static int closeSurfaceDis = 500;
    static int farSurfaceDis = 1100;

    static JFrame frame = new JFrame();
    static Canvas canvas = new Canvas(canvasLen);

    public SimpleJava3D() {
        frame.setBounds(0, 0, canvasLen, canvasLen);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1, 1));
        frame.setUndecorated(true);
        frame.add(canvas);
        Dragger.drag(frame, canvas);
    }

    public void show() {
        frame.setVisible(true);
    }

    private static Point pointOf(double x, double y, double z) {
        return new Point(x, y, 0);
    }

    private static Point pointOf(double x, double y, double z, String name) {
        return new Point(x, y, z, name);
    }


    public static void main(String[] args) {
        SimpleJava3D frame = new SimpleJava3D();
        frame.show();

        ArrayList<Point> points = new ArrayList<>();
        // surface box
        Point a0 = pointOf(-300, 300, 500, "A0");
        Point b0 = pointOf(300, 300, 500, "B0");
        Point c0 = pointOf(-300, -300, 500,  "C0");
        Point d0 = pointOf(300, -300, 500, "D0");
        points.add(a0);
        points.add(b0);
        points.add(c0);
        points.add(d0);

        // far side box
        // center: (0, 0, 3)
        Point a = pointOf(-300, 300, 1100, "A");
        Point b = pointOf(300, 300, 1100, "B");
        Point c = pointOf(-300, -300, 1100, "C");
        Point d = pointOf(300, -300, 1100, "D");

        points.add(a);
        points.add(b);
        points.add(c);
        points.add(d);


        ArrayList<Line> lines = new ArrayList<>() {
            {

                add(a, b);
                add(b, d);
                add(d, c);
                add(c, a);

                add(a0, b0);
                add(b0, d0);
                add(d0, c0);
                add(c0, a0);

                add(a0, a);
                add(b0, b);
                add(d0, d);
                add(c0, c);
            }

            private void add(Point a, Point b) {
                add(new Line(a, b));
            }
        };

        ArrayList<Triangle> triangles = new ArrayList<>();
        triangles.add(new Triangle(a0, b0, c0));

        canvas.draw(lines, triangles);
        canvas.addKeyListener(new KeyAdapter() {
            Consumer<Point> degreeDealer = null;

            @Override
            public void keyTyped(KeyEvent e) {
                int code = e.getKeyChar();
                boolean doTurn = true;
                switch (code) {
                    case 'w':
                        degreeDealer = point -> point.zyTurn(10);
                        break;
                    case 'a':
                        degreeDealer = point -> point.xzTurn(-10);
                        break;
                    case 'd':
                        degreeDealer = point -> point.xzTurn(10);
                        break;
                    case 's':
                        degreeDealer = point -> point.zyTurn(-10);
                        break;
                    case 'q':
                        degreeDealer = point -> point.xyTurn(-10);
                        break;
                    case 'e':
                        degreeDealer = point -> point.xyTurn(10);
                        break;
                    default:
                        doTurn = false;
                }
                if (doTurn) {
                    points.forEach(degreeDealer);
                    lines.forEach(Line::reset);
                    triangles.forEach(Triangle::reset);
                    canvas.draw(lines, triangles);
                }
                super.keyTyped(e);
            }
        });

        canvas.grabFocus();

        // 2D coordinator
        // box on the surface
        // function to calculate 1 unit visual length of some distance
        // another box on 1 unit length distance
        // listen command to adjust

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            if ("exit".equals(nextLine)) {
                System.out.println("Bye");
                System.exit(0);
            } else {
                String[] parts = nextLine.split(" ");
                if (parts.length >= 9 && parts[0].equals("p")) {
                    try {
                        int x = intOf(parts[1]);
                        int y = intOf(parts[2]);
                        int z = intOf(parts[3]);
                        String name = parts[4];

                        int x1 = intOf(parts[5]);
                        int y2 = intOf(parts[6]);
                        int z3 = intOf(parts[7]);
                        String name1 = parts[8];

                        Point p0 = new Point(x, y, z, name);
                        points.add(p0);
                        Point p1 = new Point(x1, y2, z3, name1);
                        points.add(p1);
                        lines.add(new Line(p0, p1));
                    } catch (Exception ignore) {

                    }

                }
            }
        }
    }

    private static int intOf(String i) {
        return Integer.parseInt(i);
    }
}
