package com.colour.board.api.colorpalette.service;

import com.colour.board.api.colorpalette.domain.dto.ColorCond;
import com.colour.board.api.colorpalette.domain.entity.HexColor;

import java.util.List;

public interface ColorService {

    HexColor createColor(HexColor color);

    HexColor findColorByHex(String hexColor);

    List<HexColor> findColorByCond(ColorCond colorCond);

    void deleteColor(Long colorId);
}
