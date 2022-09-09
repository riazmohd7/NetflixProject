package com.netflix.service;

import com.netflix.accessor.S3Accessor;
import com.netflix.accessor.VideoAccessor;
import com.netflix.accessor.model.VideoDTO;
import com.netflix.exceptions.InvalidDataException;
import com.netflix.exceptions.InvalidVideoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VideoService {

    @Autowired
    VideoAccessor videoAccessor;

    @Autowired
    S3Accessor s3Accessor;

    public String getVideoUrl(final String videoId){
        VideoDTO videoDTO = videoAccessor.getVideoByVideoId(videoId);
        if(videoDTO == null){
            throw new InvalidVideoException("Video Id: "+videoId+ " does not exist");
        }
        String videoPath = videoDTO.getVideoPath();

        return s3Accessor.getPreSignedURL(videoPath,videoDTO.getTotalLength()*60);
    }

    public String getVideoThumbnail(final String videoId){
        VideoDTO videoDTO = videoAccessor.getVideoByVideoId(videoId);
        if(videoDTO == null){
            throw new InvalidVideoException("Video Id: "+videoId+ " does not exist");
        }
        String thumbNailPath = videoDTO.getThumbNailPath();

        return s3Accessor.getPreSignedURL(thumbNailPath,videoDTO.getTotalLength()*2);
    }

}
