package com.colour.board.api.colorpalette.repository;

import com.colour.board.api.colorpalette.domain.dto.ColorCond;
import com.colour.board.api.colorpalette.domain.entity.HexColor;

import java.util.List;
import java.util.Optional;

public interface ColorRepository {

    HexColor save(HexColor color);

    Optional<HexColor> findByHex(String hexColor);

    List<HexColor> findByCond(ColorCond cond);

    void delete(Long colorId);
}
