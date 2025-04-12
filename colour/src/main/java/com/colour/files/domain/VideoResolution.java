package com.colour.files.domain;

import java.util.ArrayList;
import java.util.List;

public record VideoResolution(int width, int height) {
    private static final List<int[]> RESOLUTION_VALUES = List.of(
            new int[]{3840, 2160},
            new int[]{2560, 1440},
            new int[]{1920, 1080},
            new int[]{1280, 720},
            new int[]{854, 480}
    );
    private static final List<String[]> RESOLUTIONS = List.of(
            new String[]{"3840", "2160"},
            new String[]{"2560", "1440"},
            new String[]{"1920", "1080"},
            new String[]{"1280", "720"},
            new String[]{"854", "480"}
    );

    public List<String[]> getMaximumResolution() {
        int resolution = width * height;
        List<String[]> result = new ArrayList<>();
        for (int i = 0; i < RESOLUTION_VALUES.size(); i++) {
            int[] resValues = RESOLUTION_VALUES.get(i);
            if (resolution >= resValues[0] * resValues[1]) {
                result.add(RESOLUTIONS.get(i));
            }
        }
        return result;
    }
}
