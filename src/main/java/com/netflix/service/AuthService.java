package com.netflix.service;

import com.netflix.accessor.AuthAccessor;
import com.netflix.accessor.UserAccessor;
import com.netflix.accessor.model.UserDTO;
import com.netflix.exceptions.DependencyFailureException;
import com.netflix.exceptions.InvalidCredentialsException;
import com.netflix.security.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.http.ResponseEntity;

import java.util.Date;

@Component
public class AuthService {
    @Autowired
    UserAccessor userAccessor;
    @Autowired
    AuthAccessor authAccessor;

    /**
     *
     * @param email :Email of the user who wants to login
     * @param password: Password of the user who wants to login
     * @return : Jwt token if email and password combination is correct
     */

    public String login(final String email, final String password){
        UserDTO userDTO = userAccessor.getUserByEmail(email);
        if(userDTO!=null && userDTO.getPassword().equals(password)){
            // Generate and store token
            String token = Jwts.builder()
                    .setSubject(email)
                    .setAudience(userDTO.getRole().name())
                    .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET_KEY.getBytes())
                    .compact();
            authAccessor.storeToken(userDTO.getUserId(), token);
            return token;
        }
        throw new InvalidCredentialsException("either email or password is inCorrect");
    }
    public void logout(final String token){
        authAccessor.deleteAuthByToken(token);
    }
}
