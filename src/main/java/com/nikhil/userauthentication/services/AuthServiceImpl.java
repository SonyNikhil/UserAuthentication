package com.nikhil.userauthentication.services;

import com.nikhil.userauthentication.models.Token;
import com.nikhil.userauthentication.models.User;
import com.nikhil.userauthentication.repos.UserRepo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService
{

    UserRepo userRepo;

    public AuthServiceImpl(UserRepo userRepo){
        this.userRepo = userRepo;
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
    public User login(String email, String password){
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

        return userRepo.save(user.get());
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
