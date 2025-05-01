package com.cg.budgetboard.controller;

import com.cg.budgetboard.dto.AuthenticationRequest;
import com.cg.budgetboard.dto.RegisterDTO;
import com.cg.budgetboard.dto.ResetPasswordDTO;
import com.cg.budgetboard.dto.ResponseDTO;
import com.cg.budgetboard.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;


    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> userRegister(@Valid @RequestBody RegisterDTO registerDTO){
        ResponseDTO responseDTO=userService.registerUser(registerDTO);

        return new ResponseEntity<ResponseDTO>(responseDTO, responseDTO.getMessage().equals("error")? HttpStatus.CONFLICT: HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/forget-password/{email}")
    public ResponseEntity<ResponseDTO> forgetPassword(@PathVariable String email) {
        userService.forgetPassword(email);
        return new ResponseEntity<>(new ResponseDTO("Password reset OTP successfully sent.", null), HttpStatus.CREATED);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseDTO> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        userService.resetPassword(resetPasswordDTO);
        return new ResponseEntity<>(new ResponseDTO("Password reset successfully.", null), HttpStatus.CREATED);
    }
}
