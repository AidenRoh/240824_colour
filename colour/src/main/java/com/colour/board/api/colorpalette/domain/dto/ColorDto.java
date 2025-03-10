package com.colour.board.api.colorpalette.domain.dto;

import com.colour.member.api.users.dto.Responsible;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColorDto implements Responsible {
    private Long colorId;
    private String hexColor;
}
