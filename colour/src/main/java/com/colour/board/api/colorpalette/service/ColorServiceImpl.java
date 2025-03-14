package com.colour.board.api.colorpalette.service;

import com.colour.board.api.colorpalette.domain.entity.HexColor;
import com.colour.board.api.colorpalette.repository.ColorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ColorServiceImpl extends AbstractColorService {

    private final ColorRepository repository;

    public ColorServiceImpl(ColorRepository repository) {
        this.repository = repository;
    }

    @Override
    public HexColor createColor(HexColor color) {
        Optional<HexColor> findColor = repository.findByHex(color.getHexColor());
        return findColor.orElseGet(() -> repository.save(color));
    }

    @Override
    public Optional<HexColor> findColorByHex(String hexColor) {
        return repository.findByHex(hexColor);
    }

    @Override
    public void deleteColor(Long colorId) {
        repository.delete(colorId);
    }
}
