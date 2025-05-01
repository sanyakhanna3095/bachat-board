package com.cg.budgetboard.dto;

import lombok.Data;

@Data
public class ResetPasswordDTO {
    private String email;
    private String otp;
    private String newPassword;
}