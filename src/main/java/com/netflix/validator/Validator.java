package com.netflix.validator;

import com.netflix.accessor.ProfileAccessor;
import com.netflix.accessor.VideoAccessor;
import com.netflix.accessor.model.ProfileDTO;
import com.netflix.accessor.model.VideoDTO;
import com.netflix.exceptions.InvalidProfileException;
import com.netflix.exceptions.InvalidVideoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Validator {

    @Autowired
    private ProfileAccessor profileAccessor;

    @Autowired
    private VideoAccessor videoAccessor;

    public void validateProfile(final String profileId, final String userId){
        ProfileDTO profileDTO = profileAccessor.getProfileByProfileId(profileId);
        if(profileDTO == null || !profileDTO.getUserId().equals(userId)){
            throw new InvalidProfileException("Profile with "+profileId+ " doesnot exist or invalid");
        }
    }
    public void validateVideoId(final String videoId){
        VideoDTO videoDTO = videoAccessor.getVideoByVideoId(videoId);
        if(videoDTO == null){
            throw new InvalidVideoException("Video with videoId "+videoId+" does not exist");
        }
    }

}
