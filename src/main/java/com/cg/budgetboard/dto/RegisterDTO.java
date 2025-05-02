package com.cg.budgetboard.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public @Data class RegisterDTO {
    @NotBlank(message = "name can't be empty")
    @Size(min = 3, message = "name must have at least 3 characters")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*(?:[\\s-][A-Z][a-zA-Z]*)*$", message = "Name must start with a capital letter and should only contain alphabets")
    private String name;

    @NotBlank(message = "Email field can't be empty")
    @Email
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must be at least 8 characters long and include uppercase, lowercase, number, and special character")
    private String password;
}
