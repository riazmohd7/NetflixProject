package com.netflix.accessor.model;

import lombok.Builder;
import lombok.Getter;

import java.sql.Date;
@Builder
@Getter
public class WatchHistoryDTO {
    private String profileId;
    private String videoId;
    private double rating;
    private int watchedLength;
    private Date lastWatchedAt;
    private Date firstWatchedAt;
}
