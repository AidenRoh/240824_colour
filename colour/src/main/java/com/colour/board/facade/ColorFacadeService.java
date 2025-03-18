package com.colour.board.facade;

import com.colour.board.api.colorpalette.domain.dto.ColorDto;
import com.colour.board.api.colorpalette.domain.entity.HexColor;
import com.colour.board.api.colorpalette.service.AbstractColorService;
import com.colour.board.api.postcolor.domain.dto.PostColorDto;
import com.colour.board.api.postcolor.domain.entity.PostColor;
import com.colour.board.api.postcolor.service.PostColorService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ColorFacadeService {

    private final AbstractColorService colorService;
    private final PostColorService postColorService;

    public ColorFacadeService(AbstractColorService colorService, PostColorService postColorService) {
        this.colorService = colorService;
        this.postColorService = postColorService;
    }

    @PreAuthorize("@postOwnerValidator.validatePostOwner(#postId)")
    public HexColor createColor(ColorDto dto, Long postId) {
        HexColor color = colorService.createColor(AbstractColorService.of(dto.getHexColor()));
        //prevent duplicated request
        if (!postColorService.isPostColorExist(postId, color.getColorId())) {
            postColorService.create(new PostColor(postId, color.getColorId()));
        }
        return color;
    }

    @PreAuthorize("@postOwnerValidator.validatePostOwner(#dto.postId)")
    public void deleteColor(PostColorDto dto) {
        if (postColorService.isPostColorExist(dto.getPostId(), dto.getColorId())) {
            postColorService.delete(dto.getPostId(), dto.getColorId());
        }

        dto.setPostId(null);
        if (postColorService.findByCond(dto).isEmpty()) {
            colorService.deleteColor(dto.getColorId());
        }
    }
}
