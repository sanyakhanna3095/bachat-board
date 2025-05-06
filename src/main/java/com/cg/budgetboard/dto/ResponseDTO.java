package com.cg.budgetboard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public @Data class ResponseDTO<S, S1> {
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    public ResponseDTO(String message, Object data) {
        this.message=message;
        this.data=data;
    }

}
