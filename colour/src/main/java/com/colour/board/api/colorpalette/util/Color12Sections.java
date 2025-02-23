package com.colour.board.api.colorpalette.util;

public enum Color12Sections {
    RED(345, 7),
    RED_ORANGE(7, 22),
    ORANGE(22, 37),
    YELLOW_ORANGE(37, 52),
    YELLOW(52, 82),
    YELLOW_GREEN(82, 127),
    GREEN(127, 172),
    BLUE_GREEN(172, 217),
    BLUE(217, 255),
    BLUE_VIOLET(255, 285),
    VIOLET(285, 315),
    RED_VIOLET(315, 344);

    private final int min, max;

    Color12Sections(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public static String classify(float hue) {
        for (Color12Sections each : values()) {
            if (each.isInRange(hue)) {
                return each.name();
            }
        }
        return null;
    }

    private boolean isInRange(float hue) {
        if (this.max < this.min) {
            return (hue >= min || hue < max);
        }
        return hue >= min && hue < max;
    }
}
