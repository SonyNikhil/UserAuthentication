package com.nikhil.userauthentication.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogOutRequestDto
{
    private String tokenValue;
    private String email;
}
