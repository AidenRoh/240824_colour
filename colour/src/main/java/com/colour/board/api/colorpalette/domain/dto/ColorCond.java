package com.colour.board.api.colorpalette.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColorCond {
    private String hue;
    private Long saturation;
    private Long lightness;
}
