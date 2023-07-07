package cn.itscloudy;

class Triangle {

    Line line1;
    Line line2;
    Line line3;

    Point c;
    Point b;
    Point a;

    Triangle(Point a, Point b, Point c) {
        // sort the 3 points by x
        if (b.x < a.x) {
            Point temp = a;
            a = b;
            b = temp;
        }
        if (c.x < b.x) {
            Point temp = b;
            b = c;
            c = temp;
        }
        if (b.x < a.x) {
            Point temp = a;
            a = b;
            b = temp;
        }

        this.c = c;
        this.b = b;
        this.a = a;
        line1 = new Line(a, b);
        line2 = new Line(b, c);
        line3 = new Line(a, c);
    }

    void reset() {
        line1.reset();
        line2.reset();
        line3.reset();
    }

    // get all the points
    // calculate all the point-zero distance, sort by distance
    // paint from the fairest point to the nearest point
}
