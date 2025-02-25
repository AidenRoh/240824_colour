package com.colour.board.api.colorpalette.domain.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class HexColor {

    private Long colorId;
    private String hexColor;
    private String hue;
    private Long saturation;
    private Long lightness;
    private Timestamp createdAt;

    public HexColor(String hexColor, String hue, Long saturation, Long lightness) {
        this.hexColor = hexColor;
        this.hue = hue;
        this.saturation = saturation;
        this.lightness = lightness;
        this.createdAt = new Timestamp(new Date().getTime());
    }

    public HexColor() {
    }
}
