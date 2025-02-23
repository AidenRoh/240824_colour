package com.colour.board.api.colorpalette.util;

public enum ZoneSystem {
    ZONE_0(0, 23, 0),
    ZONE_1(23, 46, 1),
    ZONE_2(46, 70, 2),
    ZONE_3(70, 93, 3),
    ZONE_4(93, 116, 4),
    ZONE_5(116, 139, 5),
    ZONE_6(139, 162, 6),
    ZONE_7(162, 185, 7),
    ZONE_8(185, 209, 8),
    ZONE_9(209, 232, 9),
    ZONE_10(232, 255, 10);

    private final int min, max;
    private final long zone;

    ZoneSystem(int min, int max, long zone) {
        this.min = min;
        this.max = max;
        this.zone = zone;
    }

    public static Long classify(int luminance) {
        for (ZoneSystem each : values()) {
            if (each.min <= luminance && each.max > luminance) {
                return each.zone;
            }
        }
        return null;
    }
}
