package cn.itscloudy.util;

public class Range<T extends Comparable<T>> {

    T min;
    T max;
    public Range(T a, T b) {
        if (a.compareTo(b) <= 0) {
            this.min = a;
            this.max = b;
        } else {
            this.min = b;
            this.max = a;
        }
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }
}
