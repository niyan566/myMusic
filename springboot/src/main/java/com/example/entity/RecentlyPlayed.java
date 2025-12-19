package com.example.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecentlyPlayed {
    private Integer id;
    private Integer userId;
    private Integer musicId;
    private LocalDateTime playTime;
}
