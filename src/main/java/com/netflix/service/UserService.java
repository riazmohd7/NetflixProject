package com.netflix.service;

import com.netflix.accessor.EmailAccessor;
import com.netflix.accessor.OtpAccessor;
import com.netflix.accessor.UserAccessor;
import com.netflix.accessor.model.*;
import com.netflix.exceptions.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class UserService {

    @Autowired
    UserAccessor userAccessor;

    @Autowired
    OtpAccessor otpAccessor;

    @Autowired
    EmailAccessor emailAccessor;

    public void addNewUser(final String email, final String name, final String password, final String phoneNo){
        if(phoneNo.length()!=10){
            throw new InvalidDataException("Phone no "+phoneNo+ " is invalid");
        }
        if(password.length()<6){
            throw new InvalidDataException("password length is too simple");
        }
        if(name.length()<5){
            throw new InvalidDataException("name is not correct");
        }
        String emailRegex = "^[a-zA-Z0-9+&*-]+(?:\\." +
                            "[a-zA-Z0-9_+&*-]+)*@" +
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                            "A-Z]{2,7}$";
        Pattern pattern =  Pattern.compile(emailRegex);
        if(!pattern.matcher(email).matches()){
            throw new InvalidDataException("Email is not correct!");
        }

        UserDTO userDTO = userAccessor.getUserByEmail(email);
        if(userDTO!=null){
            throw new InvalidDataException("user with given email: "+email+ " already exists");
        }
        userDTO = userAccessor.getUserByPhoneNo(phoneNo);
        if(userDTO!=null){
            throw new InvalidDataException("user with given phoneNo: "+phoneNo+ " already exists");
        }
        userAccessor.addNewUser(email, name, password, phoneNo, UserState.ACTIVE, UserRole.ROLE_USER);
    }
    public void activateSubscription(){
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDTO = (UserDTO)authentication.getPrincipal();
        userAccessor.updateRole(userDTO.getUserId(), UserRole.ROLE_CUSTOMER);

    }

    public void deleteSubscription(){
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDTO = (UserDTO)authentication.getPrincipal();
        userAccessor.updateRole(userDTO.getUserId(), UserRole.ROLE_USER);

    }
    public void verifyEmail(final String otp){
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDTO = (UserDTO)authentication.getPrincipal();
        if(userDTO.getEmailVerificationStatus().equals(EmailVerificationStatus.UNVERIFIED)){
            OtpDTO otpDTO = otpAccessor.getUnusedOtp(userDTO.getUserId(), otp, OtpSentTo.EMAIL);
            if(otpDTO!=null){
                userAccessor.updateEmailVerificationStatus(userDTO.getUserId(), EmailVerificationStatus.VERIFIED);
                otpAccessor.updateOtpStatus(otpDTO.getOtpId(),OtpState.USED);
            }
            else{
                throw new InvalidDataException("Otp does not exist");
            }


        }
    }
    public void verifyPhone(final String otp){
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDTO = (UserDTO)authentication.getPrincipal();
        if(userDTO.getEmailVerificationStatus().equals(PhoneVerificationStatus.UNVERIFIED)){
            OtpDTO otpDTO = otpAccessor.getUnusedOtp(userDTO.getUserId(), otp, OtpSentTo.PHONE);
            if(otpDTO!=null){
                userAccessor.updatePhoneVerificationStatus(userDTO.getUserId(), PhoneVerificationStatus.VERIFIED);
                otpAccessor.updateOtpStatus(otpDTO.getOtpId(),OtpState.USED);

            }
            else{
                throw new InvalidDataException("Otp does not exist");
            }
        }
    }
    
    public void sendEmailOtp(){
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDTO = (UserDTO)authentication.getPrincipal();
        String otp = generateOtp();
        otpAccessor.addNewOtp(userDTO.getUserId(), otp, OtpSentTo.EMAIL);
        emailAccessor.sendEmail(userDTO.getName(), userDTO.getEmail(), "OTP for email verification",
            "Your OTP for verifying the email is "+otp );
    }

    private String generateOtp(){
        int min = 100000;
        int max = 999999;
        int otp = (int)(Math.random()*(max - min + 1) + min);
        return Integer.toString(otp);
    }

}
