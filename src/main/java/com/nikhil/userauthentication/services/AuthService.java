package com.nikhil.userauthentication.services;

import com.nikhil.userauthentication.models.Token;
import com.nikhil.userauthentication.models.User;
import org.springframework.stereotype.Service;


public interface AuthService
{
    User signUp(String name, String email, String PhoneNumber, String password);
    Token login(String email, String password);
    boolean logout(Token token);
    User validateToken(String tokenValue);
}
