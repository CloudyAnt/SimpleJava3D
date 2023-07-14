package cn.itscloudy.sj3d.legacy;

public enum Quality {
    ULTRA_HIGH(1),
    VERY_HIGH(2),
    HIGH(4),
    MEDIUM(8),
    LOW(16),
    LOWEST(32)
    ;
    final int lineWidth;
    final int rectLength;

    final int paintingOffset;

    static Quality current = HIGH;

    Quality(int lineWidth) {
        this.lineWidth = lineWidth;
        this.rectLength = Math.max(1, lineWidth / 2);
        paintingOffset = rectLength / 2;
    }
}
