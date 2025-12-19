package com.example.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LikedMusic {
    private Integer id;
    private Integer userId;
    private Integer musicId;
    private LocalDateTime createTime;
}
