package com.netflix.Mapper;

import com.netflix.accessor.S3Accessor;
import com.netflix.accessor.model.ShowDTO;
import com.netflix.controller.model.ShowOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShowMapper {

    @Autowired
    S3Accessor s3Accessor;

    public ShowOutput mapShowDtoToOutput(final ShowDTO input){
        ShowOutput showOutput = ShowOutput.builder()
                .showId(input.getShowId())
                .name(input.getName())
                .typeOfShow(input.getTypeOfShow())
                .genre(input.getGenre())
                .audience(input.getAudience())
                .rating(input.getRating())
                .length(input.getLength())
                .thumbnailLink(s3Accessor.getPreSignedURL(input.getThumbnailPath(), 5*60))
                .build();
        return showOutput;
    }

}
