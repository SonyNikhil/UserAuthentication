package com.nikhil.userauthentication.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto
{
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
}
