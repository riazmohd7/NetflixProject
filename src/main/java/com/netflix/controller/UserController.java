package com.netflix.controller;

import com.netflix.controller.model.CreateUserInput;
import com.netflix.controller.model.VerifyEmailInput;
import com.netflix.controller.model.VerifyPhoneInput;
import com.netflix.exceptions.InvalidDataException;
import com.netflix.security.Roles;
import com.netflix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping("/user")
    @Secured({Roles.Customer, Roles.User})
    public ResponseEntity<String> addNewUser(@RequestBody CreateUserInput createUserInput){
        String name = createUserInput.getName();
        String email = createUserInput.getEmail();
        String password = createUserInput.getPassword();
        String phoneNo = createUserInput.getPhoneNo();
        try{
            userService.addNewUser(email, name, password,phoneNo);
            return ResponseEntity.status(HttpStatus.OK).body("User created successfully!");
        }
        catch (InvalidDataException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
    @PostMapping("/user/subscription")
    @Secured({Roles.User})
    public String activateSubscription(){
        userService.activateSubscription();
        return "Subscription activated Successfully";
    }

    @DeleteMapping("/user/subscription")
    @Secured({Roles.Customer})
    public String deleteSubscription(){
        userService.deleteSubscription();
        return "Subscription deleted Successfully";
    }

    @PostMapping("/user/email")
    @Secured({Roles.User, Roles.Customer})
    public ResponseEntity<String> verifyEmail(@RequestBody VerifyEmailInput verifyEmailInput){
        try {
            userService.verifyEmail(verifyEmailInput.getOtp());
            return ResponseEntity.status(HttpStatus.OK).body("Email Verified Successfully");
        }
        catch(InvalidDataException ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/user/phoneNo")
    @Secured({Roles.User, Roles.Customer})
    public ResponseEntity<String> verifyPhone(@RequestBody VerifyPhoneInput verifyPhoneInput){
        try {
            userService.verifyEmail(verifyPhoneInput.getOtp());
            return ResponseEntity.status(HttpStatus.OK).body("Email Verified Successfully");
        }
        catch(InvalidDataException ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/user/email")
    @Secured({Roles.User, Roles.Customer})
    public ResponseEntity<String> getEmailOtp(){
        try{
            userService.sendEmailOtp();
            return ResponseEntity.status(HttpStatus.OK).body("OTP sent successfully!");
        }
        catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());

        }
    }

    
}
