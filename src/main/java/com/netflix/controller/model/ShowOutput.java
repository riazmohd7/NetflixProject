package com.netflix.controller.model;

import com.netflix.accessor.model.ShowAudience;
import com.netflix.accessor.model.ShowGenre;
import com.netflix.accessor.model.ShowType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ShowOutput {
    private String showId;
    private String name;
    private ShowType typeOfShow;
    private ShowGenre genre;
    private ShowAudience audience;
    private double rating;
    private int length;
    private String thumbnailLink;
}
