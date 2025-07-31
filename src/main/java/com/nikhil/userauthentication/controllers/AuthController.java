package com.nikhil.userauthentication.controllers;

import com.nikhil.userauthentication.dtos.*;
import com.nikhil.userauthentication.models.Token;
import com.nikhil.userauthentication.models.User;
import com.nikhil.userauthentication.repos.TokenRepo;
import com.nikhil.userauthentication.repos.UserRepo;
import com.nikhil.userauthentication.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.function.LongFunction;

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
        Token token = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        LoginResponseDto loginResponseDto = new LoginResponseDto();

        loginResponseDto.setTokenValue(token.getTokenValue());

        return loginResponseDto;

    }

    @PostMapping("/logout")
    public boolean logout(@RequestBody LogOutRequestDto logOutRequestDto)
    {
        return authService.logout(logOutRequestDto.getTokenValue());
    }

    @PostMapping("/logoutAllDevices")
    public boolean logoutFromAllDevice(@RequestBody LogOutRequestDto logOutRequestDto)
    {

        if(authService.logoutFromAllDevice(logOutRequestDto.getEmail())){
             return true;
         }

        return false;
    }

    public void validateToken(){}
}
