package cn.itscloudy.sj3d.util;

import java.util.function.Function;

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

    public <U extends Comparable<U>> Range<U> mutate(Function<T, U> mutator) {
        return new Range<>(mutator.apply(min), mutator.apply(max));
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }
}
