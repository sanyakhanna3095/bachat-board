package com.cg.budgetboard.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public @Data class ResponseDTO<S, S1> {
    private String message;
    private Object data;

    public ResponseDTO(String message, Object data) {
        this.message=message;
        this.data=data;
    }

}
