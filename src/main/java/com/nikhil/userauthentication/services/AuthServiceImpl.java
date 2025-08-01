package com.nikhil.userauthentication.services;

import com.nikhil.userauthentication.models.Token;
import com.nikhil.userauthentication.models.User;
import com.nikhil.userauthentication.repos.TokenRepo;
import com.nikhil.userauthentication.repos.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.swing.text.html.Option;
import java.nio.charset.StandardCharsets;
import java.security.spec.DSAPublicKeySpec;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService
{

    // Dummy Secret Key for testing purpose

    private static final String SECRET_KEY_STRING = "just-a-dummy-secret-key-for-testing-purpose.";

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes(StandardCharsets.UTF_8));

    // Industry practised best secret key. for actual working.
//    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME_IN_MS = 10*60*60*1000;  // 10 hours

    private final UserRepo userRepo;
    private final TokenRepo tokenRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public AuthServiceImpl(UserRepo userRepo, TokenRepo tokenRepo, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
//        newUser.setPassword(password);

        newUser.setPassword(bCryptPasswordEncoder.encode(password));
        return userRepo.save(newUser);
    }

    @Override
    public Token login(String email, String password){
        Optional<User> userOptional = userRepo.findByEmail(email);

        if(userOptional.isEmpty()){
            //throw exception
            return null;
        }

        User user = userOptional.get();


        //Without BCrypt method
//        if (!user.get().getPassword().equals(password)) {
//            //throw exception
//
//            return null;
//        }

        //With BCrypt

        if(!bCryptPasswordEncoder.matches(password, userOptional.get().getPassword())){
            //throw exception
            return null;
        }

        // generate Token

        /* --------------------JWT TOKEN CODE START------------------------------*/

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME_IN_MS);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("name", user.getName());

        String jsonString = Jwts.builder().setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();


        /*----------------------JWT TOKEN CODE END------------------------------*/

        Token token = new Token();
        token.setUser(user);

        // Earlier direct method
//        token.setTokenValue(RandomStringUtils.randomAlphanumeric(128));

//        token.setTokenValue(UUID.randomUUID().toString());

        // JWT Way -


//        Calendar calendar =  Calendar.getInstance();
//
//        calendar.add(Calendar.DAY_OF_MONTH, 5);
//
//        Date date = calendar.getTime();
//
//        token.setExpiryAt(date);
//
        token.setTokenValue(jsonString);

        token.setExpiryAt(expiryDate);

        return token;

//        return tokenRepo.save(token);

    }

    @Override
    @Transactional
    public boolean logout(String token){
        Optional<Token> optionalToken = tokenRepo.findByTokenValue(token);

        if(optionalToken.isEmpty()){
            return false;
        }

        tokenRepo.deleteToken(token);

        return true;
    }


    @Override
    public User validateToken(String tokenValue){


       if(tokenValue == null || tokenValue.isEmpty()){
           return null;
        }

       Claims claims;

       try{
           claims = Jwts.parser()
                   .setSigningKey(SECRET_KEY)
                   .build()
                   .parseClaimsJws(tokenValue)
                   .getBody();
       }
       catch (io.jsonwebtoken.ExpiredJwtException e){
           System.out.println("TOKEN VALIDATION FAILED!!! (checked inside validateToken Method). Token is expired"+ e.getMessage());
           return null;
       }
       catch (io.jsonwebtoken.JwtException e){
           System.out.println("TOKEN VALIDATION FAILED!!! (checked inside validateToken Method). Invalid JWT Token"+ e.getMessage());
           return null;
       }

       String email = claims.getSubject();

       if(email == null || email.isEmpty()){
           System.out.println("TOKEN VALIDATION FAILED!!! (checked inside validateToken Method). email is null or empty in provided token");
           return null;
       }

       Optional<User> optionalUser = userRepo.findByEmail(email);

       if(optionalUser.isEmpty() || optionalUser.get().isDeleted()){
           System.out.println("TOKEN VALIDATION FAILED!!! (checked inside validateToken Method). User not found in DB");
           return null;
       }



       return optionalUser.get();
    }

    public User validateTokenInDB(String tokenValue){

        /**
         * Things that need to be checked for token validation -
         * 1. Exists in DB.
         * 2. Not Deleted.
         * 3. Not expired.
         */

        Optional<Token> optionalToken =
                tokenRepo.findByTokenValueAndIsDeletedAndExpiryAtGreaterThan (tokenValue, false,new Date());

        if(optionalToken.isEmpty()){
            // throw exceptoin

            return null;
        }

        Token token = optionalToken.get();
        return token.getUser();
    }

    @Transactional
    @Override
    public boolean logoutFromAllDevice(String email){
        Optional<User> optionalUser = userRepo.findByEmail(email);

        if(optionalUser.isEmpty()){
            //throw exception

            return false;
        }

        int id = optionalUser.get().getId();

        tokenRepo.deleteAllToken(optionalUser.get().getId());

        return true;
    }

}
