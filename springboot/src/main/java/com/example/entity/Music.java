package com.example.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Music {
    private int id;
    private String title;
    private String path;
    private String singer;
    private String coverPath;//封面图片路径
    private int duration;
    private LocalDateTime uploadTime;
}
