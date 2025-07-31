package com.nikhil.userauthentication.services;

import com.nikhil.userauthentication.models.Token;
import com.nikhil.userauthentication.models.User;
import com.nikhil.userauthentication.repos.TokenRepo;
import com.nikhil.userauthentication.repos.UserRepo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService
{

    private final UserRepo userRepo;
    private final TokenRepo tokenRepo;


    public AuthServiceImpl(UserRepo userRepo, TokenRepo tokenRepo){
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
    }

    @Override
    public User signUp(String name, String email, String phoneNumber, String password){

        Optional<User> user = userRepo.findByEmail(email);

        if(user.isPresent()){

            //throw exception
            return null;
        }

        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPhoneNumber(phoneNumber);
        newUser.setPassword(password);

        return userRepo.save(newUser);
    }

    @Override
    public Token login(String email, String password){
        Optional<User> user = userRepo.findByEmail(email);

        if(user.isEmpty()){
            //throw exception
            return null;
        }

        if (!user.get().getPassword().equals(password)) {
            //throw exception

            return null;
        }

        // generate Token

        Token token = new Token();
        token.setUser(user.get());

        token.setTokenValue(RandomStringUtils.randomAlphanumeric(128));

//        token.setTokenValue(UUID.randomUUID().toString());

        Calendar calendar =  Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_MONTH, 5);

        Date date = calendar.getTime();

        token.setExpiryAt(date);

        return tokenRepo.save(token);

    }

    @Override
    public boolean logout(Token token){
        return false;
    }

    @Override
    public User validateToken(String tokenValue){
        return null;
    }

}
