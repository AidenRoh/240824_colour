package com.colour.board.api.colorpalette.util;

public enum Saturation {

    LEVEL_0(0),
    LEVEL_1(1),
    LEVEL_2(2),
    LEVEL_3(3),
    LEVEL_4(4),
    LEVEL_5(5),
    LEVEL_6(6),
    LEVEL_7(7),
    LEVEL_8(8),
    LEVEL_9(9),
    LEVEL_10(10);

    private long value;

    Saturation(long value) {
        this.value = value;
    }

    public static Long classify(int saturation) {
        for (Saturation each : values()) {
            if (each.value == saturation) {
                return each.value;
            }
        }
        return null;
    }

}
