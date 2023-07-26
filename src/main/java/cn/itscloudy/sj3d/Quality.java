package cn.itscloudy.sj3d;

public enum Quality {
    ULTRA_HIGH(1),
    VERY_HIGH(2),
    HIGH(4),
    MEDIUM(8),
    LOW(16),
    LOWEST(32)
    ;
    public final int lineWidth;
    public final int rectLength;
    public final int paintingOffset;
    public static Quality current = HIGH;

    Quality(int lineWidth) {
        this.lineWidth = lineWidth;
        this.rectLength = Math.max(1, lineWidth / 2);
        paintingOffset = rectLength / 2;
    }
}
