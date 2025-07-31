package com.nikhil.userauthentication.controllers;

import com.nikhil.userauthentication.dtos.*;
import com.nikhil.userauthentication.models.User;
import com.nikhil.userauthentication.services.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController
{

    AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }
    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignUpRequestDto signUpRequestDto)
    {
        User user = authService.signUp(signUpRequestDto.getName(), signUpRequestDto.getEmail(),
                signUpRequestDto.getPhoneNumber(), signUpRequestDto.getPassword());

        return UserDto.from(user);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto)
    {
        return null;
    }

    @PostMapping("/logout")
    public boolean logout(@RequestBody LogOutRequestDto logOutRequestDto)
    {
        return false;
    }

    public void validateToken(){}
}
