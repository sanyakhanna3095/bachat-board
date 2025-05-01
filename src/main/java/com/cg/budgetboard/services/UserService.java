package com.cg.budgetboard.services;


import com.cg.budgetboard.dto.AuthenticationRequest;
import com.cg.budgetboard.dto.RegisterDTO;
import com.cg.budgetboard.dto.ResetPasswordDTO;
import com.cg.budgetboard.dto.ResponseDTO;
import com.cg.budgetboard.exceptionhandler.CustomException;
import com.cg.budgetboard.model.User;
import com.cg.budgetboard.repository.UserRepository;
import com.cg.budgetboard.util.JwtUtility;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class UserService implements UserInterface {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtility jwtUtility;
    @Autowired
    private EmailService emailService;


    @Override
    public ResponseDTO<String ,String> registerUser(RegisterDTO registerDTO){
        log.info("Registering user:{}", registerDTO.getEmail());
        ResponseDTO<String,String> res=new ResponseDTO<>();
        if(existsByEmail(registerDTO.getEmail())){
            log.warn("Registration failed: User already exists within email {}", registerDTO.getEmail());
            res.setMessage("error");
            res.setData("User already Exists");
            return res;
        }

        User user=new User();
        user.setName(registerDTO.getName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        userRepository.save(user);
        log.info("User {} registered successfuly!", user.getEmail());
        res.setMessage("success");
        res.setData("User registered successfuly!");
        return res;
    }

    @Override
    public boolean existsByEmail(@NotBlank(message = "Email field can't be empty") @Email String email) {
        log.debug("Checking if user exists using email:{}", email);
        return userRepository.findByEmail(email).isPresent();
    }

    public String login(AuthenticationRequest authenticationrequest) {
        User user = userRepository.findByEmail(authenticationrequest.getEmail())
                .orElseThrow(() -> new CustomException("Invalid email or user not registered."));

        if (!passwordEncoder.matches(authenticationrequest.getPassword(), user.getPassword())) {
            throw new CustomException("Invalid password.");
        }
        return "Token: "+jwtUtility.generateToken(user.getEmail());
    }

    public void forgetPassword(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new CustomException("No user found with email: " + email);
        }

        User user = userOptional.get();
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        user.setOtp(otp);
        userRepository.save(user);

        String message = "Dear " + user.getName()
                + "\nYour OTP for password reset is: " + otp
                + "\nPlease use this OTP to set your new password.";
        emailService.sendEmail(user.getEmail(), "Password Reset OTP", message);
    }

    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {
        Optional<User> userOptional = userRepository.findByEmail(resetPasswordDTO.getEmail());
        if (userOptional.isEmpty()) {
            throw new CustomException("No user found with email: " + resetPasswordDTO.getEmail());
        }

        User user = userOptional.get();
        if (!resetPasswordDTO.getOtp().equals(user.getOtp())) {
            throw new CustomException("Invalid OTP provided.");
        }

        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
        user.setOtp(null);
        userRepository.save(user);
    }
}
