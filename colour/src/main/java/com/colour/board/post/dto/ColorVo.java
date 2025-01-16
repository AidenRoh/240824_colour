package com.colour.board.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColorVo{

    private String color;

    @Override
    public String toString() {
        return color;
    }
}
