package com.example.entity;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class PlayList {
    private Integer id;
    private Integer userId;
    private String title;
    private String path;
    private String describe;
    private LocalDateTime createTime;
}
