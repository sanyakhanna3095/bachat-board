package com.cg.budgetboard.services;

import com.cg.budgetboard.dto.RegisterDTO;
import com.cg.budgetboard.dto.ResponseDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface UserInterface {
    ResponseDTO<String ,String> registerUser(RegisterDTO registerDTO);

    boolean existsByEmail(@NotBlank(message = "Email field can't be empty") @Email String email);
}
