package com.colour.board.api.colorpalette.service;

import com.colour.board.api.colorpalette.domain.dto.ColorCond;
import com.colour.board.api.colorpalette.domain.entity.HexColor;
import com.colour.board.api.colorpalette.util.Color12Sections;
import com.colour.board.api.colorpalette.util.Saturation;
import com.colour.board.api.colorpalette.util.ZoneSystem;

import java.awt.*;
import java.util.Locale;
import java.util.Optional;

public abstract class AbstractColorService {

    public static float REC_709_RED_COEFFICIENT = 0.2126F;
    public static float REC_709_GREEN_COEFFICIENT = 0.7152F;
    public static float REC_709_BLUE_COEFFICIENT = 0.0722F;

    public abstract HexColor createColor(HexColor color);

    public abstract Optional<HexColor> findColorByHex(String hexColor);

    public abstract void deleteColor(Long colorId);

    public static HexColor of(String value) {
        String hexKey = value.toUpperCase(Locale.ROOT);
        Color hexColor = Color.decode(hexKey);
        float[] hsb = Color.RGBtoHSB(hexColor.getRed(), hexColor.getGreen(), hexColor.getBlue(), null);
        return new HexColor(hexKey,
                Color12Sections.classify(getHue(hsb)),
                Saturation.classify(getSaturation(hsb)),
                ZoneSystem.classify(getLuminance(hexColor))
        );
    }

    public static ColorCond getColorCond(String value) {
        ColorCond colorCond = new ColorCond();
        String hexKey = value.toUpperCase(Locale.ROOT);
        Color hexColor = Color.decode(hexKey);
        float[] hsb = Color.RGBtoHSB(hexColor.getRed(), hexColor.getGreen(), hexColor.getBlue(), null);
        colorCond.setHue(Color12Sections.classify(getHue(hsb)));
        colorCond.setSaturation(Saturation.classify(getSaturation(hsb)));
        colorCond.setLightness(ZoneSystem.classify(getLuminance(hexColor)));
        return colorCond;
    }

    private static int getHue(float[] hsb) {
        return Math.round(hsb[0] * 360);
    }

    private static int getSaturation(float[] hsb) {
        return Math.round(hsb[1] * 10);
    }

    private static int getLuminance(Color color) {
        return Math.round(REC_709_RED_COEFFICIENT * color.getRed() +
                REC_709_GREEN_COEFFICIENT * color.getGreen() +
                REC_709_BLUE_COEFFICIENT * color.getBlue());
    }
}
