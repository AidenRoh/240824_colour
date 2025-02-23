package com.colour.board.api.colorpalette.service;

import com.colour.board.api.colorpalette.domain.dto.ColorCond;
import com.colour.board.api.colorpalette.domain.dto.ColorDto;
import com.colour.board.api.colorpalette.domain.entity.HexColor;
import com.colour.board.api.colorpalette.util.Color12Sections;
import com.colour.board.api.colorpalette.util.Saturation;
import com.colour.board.api.colorpalette.util.ZoneSystem;

import java.awt.*;

public abstract class AbstractColorService {

    public static float REC_709_RED_COEFFICIENT = 0.2126F;
    public static float REC_709_GREEN_COEFFICIENT = 0.7152F;
    public static float REC_709_BLUE_COEFFICIENT = 0.0722F;

    public abstract HexColor createColor(HexColor color);

    public abstract HexColor findColorByHex(String hexColor);

    public abstract HexColor findColorByCond(ColorCond colorCond);

    public abstract void deleteColor(Long colorId);

    public static HexColor of(ColorDto colorDto) {
        String hexColor = colorDto.getHexColor();
        Color hex = Color.decode(hexColor);
        float[] hsb = Color.RGBtoHSB(hex.getRed(), hex.getGreen(), hex.getBlue(), null);

        return new HexColor(hexColor,
                Color12Sections.classify(getHue(hsb)),
                Saturation.classify(getSaturation(hsb)),
                ZoneSystem.classify(getLuminance(hex))
        );
    }

    private static int getHue(float[] hsb) {
        return Math.round(hsb[0] * 360);
    }

    private static int getSaturation(float[] hsb) {
        return Math.round(hsb[1] * 10);
    }

    private static int getLuminance(Color color) {
        return Math.round(
                REC_709_RED_COEFFICIENT * color.getRed() +
                        REC_709_GREEN_COEFFICIENT * color.getGreen() +
                        REC_709_BLUE_COEFFICIENT * color.getBlue()
        );
    }
}
