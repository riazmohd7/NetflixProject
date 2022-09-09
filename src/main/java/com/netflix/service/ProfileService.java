package com.netflix.service;

import com.netflix.accessor.ProfileAccessor;
import com.netflix.accessor.model.ProfileType;
import com.netflix.accessor.model.UserDTO;
import com.netflix.controller.model.ProfileTypeInput;
import com.netflix.exceptions.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ProfileService {
    @Autowired
    private ProfileAccessor profileAccessor;

    public void activateProfile(final String name, final ProfileTypeInput type){
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDTO = (UserDTO)authentication.getPrincipal();
        if(name.length()<5 || name.length() > 25){
            throw new InvalidDataException("Name length should be between 5 and 25");
        }
        profileAccessor.addNewProfile(userDTO.getUserId(), name, ProfileType.valueOf(type.name()));
    }

    public void deactivateProfile(final String profileId){
        profileAccessor.deleteProfile(profileId);
    }
}
