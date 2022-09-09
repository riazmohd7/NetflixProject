package com.netflix.controller;

import com.netflix.controller.model.GetWatchHistoryInput;
import com.netflix.controller.model.WatchHistoryInput;
import com.netflix.exceptions.InvalidProfileException;
import com.netflix.exceptions.InvalidVideoException;
import com.netflix.security.Roles;
import com.netflix.service.WatchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
public class WatchHistoryController {

    @Autowired
    private WatchHistoryService watchHistoryService;

    @PostMapping("/video/{videoId}/watchTime")
    @Secured({Roles.Customer})
    public ResponseEntity<Void> updateWatchHistory(@PathVariable("videoId") String videoId,
                                                   @RequestBody WatchHistoryInput watchHistoryInput){
        String profileId = watchHistoryInput.getProfileId();
        int watchedLength = watchHistoryInput.getWatchTime();
        try{
            watchHistoryService.updateWatchHistory(profileId, videoId, watchedLength);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch(InvalidVideoException | InvalidProfileException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/video/{videoId}/watchTime")
    @Secured({Roles.Customer})
    public ResponseEntity<Integer> getWatchHistory(@PathVariable("videoId") String videoId,
                                                   @RequestBody GetWatchHistoryInput input){
        String profileId = input.getProfileId();
        try{
            int watchLength = watchHistoryService.getWatchedHistory(profileId, videoId);
            return ResponseEntity.status(HttpStatus.OK).body(watchLength);
        }
        catch(InvalidVideoException | InvalidProfileException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
