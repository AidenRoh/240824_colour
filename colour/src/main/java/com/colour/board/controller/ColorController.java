package com.colour.board.controller;

import com.colour.board.api.colorpalette.domain.dto.ColorDto;
import com.colour.board.api.colorpalette.domain.entity.HexColor;
import com.colour.board.api.postcolor.domain.dto.PostColorDto;
import com.colour.board.facade.ColorFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("color")
@RequiredArgsConstructor
public class ColorController {

    private final ColorFacadeService service;

    @PostMapping("/{postId}/create")
    public ResponseEntity<HexColor> createColor(@ModelAttribute ColorDto dto,
                                                @PathVariable Long postId) {
        HexColor color = service.createColor(dto, postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(color);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteColor(@ModelAttribute PostColorDto dto) {
        service.deleteColor(dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Color deleted");
    }
}
