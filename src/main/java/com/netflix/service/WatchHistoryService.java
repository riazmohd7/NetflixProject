package com.netflix.service;

import com.netflix.accessor.VideoAccessor;
import com.netflix.accessor.WatchHistoryAccessor;
import com.netflix.accessor.model.UserDTO;
import com.netflix.accessor.model.VideoDTO;
import com.netflix.accessor.model.WatchHistoryDTO;
import com.netflix.exceptions.InvalidDataException;
import com.netflix.exceptions.InvalidVideoException;
import com.netflix.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class WatchHistoryService {

    @Autowired
    private Validator validator;

    @Autowired
    private WatchHistoryAccessor watchHistoryAccessor;

    @Autowired
    private VideoAccessor videoAccessor;

    public void updateWatchHistory(final String profileId, final String videoId,
                                   final int watchedLength){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDTO = (UserDTO)authentication.getPrincipal();

        validator.validateProfile(profileId, userDTO.getUserId());
        VideoDTO videoDTO = videoAccessor.getVideoByVideoId(videoId);
        if(watchedLength < 1 || watchedLength > videoDTO.getTotalLength()){
            throw new InvalidDataException("Watched Length of "+watchedLength+" is invalid");
        }
        WatchHistoryDTO watchHistoryDTO = watchHistoryAccessor.getWatchedHistory(profileId, videoId);
        //Inserting for the first time
        if(watchHistoryDTO == null){
            watchHistoryAccessor.insertWatchHistory(profileId, videoId, 0.0, watchedLength);
        }
        else {
            watchHistoryAccessor.updateWatchedLength(profileId, videoId, watchedLength);
        }
    }
    public int getWatchedHistory(final String profileId, final String videoId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDTO = (UserDTO)authentication.getPrincipal();

        validator.validateProfile(profileId, userDTO.getUserId());
        VideoDTO videoDTO = videoAccessor.getVideoByVideoId(videoId);
        WatchHistoryDTO watchHistoryDTO = watchHistoryAccessor.getWatchedHistory(profileId, videoId);
        if(watchHistoryDTO!=null){
            return watchHistoryDTO.getWatchedLength();
        }
        else {
            return 0;
        }
    }
}
