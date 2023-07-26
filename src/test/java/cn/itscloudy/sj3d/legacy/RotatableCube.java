package cn.itscloudy.sj3d.legacy;

import cn.itscloudy.sj3d.Quality;
import cn.itscloudy.sj3d.util.Dragger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Generate a rotatable cube. <br/>
 * Use w, a, d, s, q, r to rotate. <br/>
 * Change quality: quality HIGH <br/>
 * There are 6 qualities: ULTRA, VERY_HIGH, HIGH, MEDIUM, LOW, LOWEST
 */
public class RotatableCube {
    static int canvasLen = 601;

    static JFrame frame = new JFrame();
    static {
        Canvas.instance = new Canvas(canvasLen);
    }
    static cn.itscloudy.sj3d.legacy.Canvas canvas = Canvas.instance;

    public RotatableCube() {
        frame.setBounds(0, 0, canvasLen, canvasLen);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1, 1));
        frame.setUndecorated(true);
        frame.add(canvas);
        Dragger.drag(frame, canvas);
    }

    public void show() {
        frame.setVisible(true);
    }

    private static SatellitePoint pointOf(double x, double y, double z) {
        return new SatellitePoint(x, y, 0);
    }

    private static SatellitePoint pointOf(double x, double y, double z, String name) {
        return new SatellitePoint(x, y, z, name);
    }


    public static void main(String[] args) {
        RotatableCube frame = new RotatableCube();
        frame.show();

        ArrayList<SatellitePoint> points = new ArrayList<>();
        // surface box
        SatellitePoint a0 = pointOf(-300, 300, 500, "A0");
        SatellitePoint b0 = pointOf(300, 300, 500, "B0");
        SatellitePoint c0 = pointOf(300, -300, 500, "C0");
        SatellitePoint d0 = pointOf(-300, -300, 500,  "D0");
        points.add(a0);
        points.add(b0);
        points.add(c0);
        points.add(d0);

        // far side box
        // center: (0, 0, 3)
        SatellitePoint a = pointOf(-300, 300, 1100, "A");
        SatellitePoint b = pointOf(300, 300, 1100, "B");
        SatellitePoint c = pointOf(300, -300, 1100, "C");
        SatellitePoint d = pointOf(-300, -300, 1100, "D");

        points.add(a);
        points.add(b);
        points.add(c);
        points.add(d);


        ArrayList<Line> lines = new ArrayList<>() {
            {

                add(a, b);
                add(b, c);
                add(c, d);
                add(d, a);

                add(a0, b0);
                add(b0, c0);
                add(c0, d0);
                add(d0, a0);

                add(a0, a);
                add(b0, b);
                add(c0, c);
                add(d0, d);
            }

            private void add(SatellitePoint a, SatellitePoint b) {
                add(new Line(a, b));
            }
        };

        ArrayList<Triangle> triangles = new ArrayList<>();
        triangles.add(new Triangle(b0, d0, a0, Color.BLUE));
        triangles.add(new Triangle(b, d, c, Color.GREEN));
        triangles.add(new Triangle(c, b0, b, Color.YELLOW));
        triangles.add(new Triangle(a0, d, d0, Color.MAGENTA));

        canvas.draw(lines, triangles);
        canvas.addKeyListener(new KeyAdapter() {
            Consumer<SatellitePoint> degreeDealer = null;

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
                    canvas.draw(lines);
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

                        SatellitePoint p0 = new SatellitePoint(x, y, z, name);
                        points.add(p0);
                        SatellitePoint p1 = new SatellitePoint(x1, y2, z3, name1);
                        points.add(p1);
                        lines.add(new Line(p0, p1));
                    } catch (Exception ignore) {

                    }

                } else if (parts[0].equals("quality")) {
                    try {
                        Quality.current = Quality.valueOf(parts[1]);
                        lines.forEach(Line::reset);
                        triangles.forEach(Triangle::reset);
                        canvas.draw(lines);
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
