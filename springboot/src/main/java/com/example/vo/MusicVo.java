package com.example.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MusicVo {
    private int id;
    private String title;
    private String path;
    private String singer;
    private String coverPath;//封面图片路径
    private int duration;
    private LocalDateTime uploadTime;
    private Boolean isLiked;
}
