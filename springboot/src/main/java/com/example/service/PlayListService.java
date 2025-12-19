package com.example.service;

import com.example.entity.PlayList;
import com.example.mapper.PlayListMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlayListService {

    @Resource
    private PlayListMapper playListMapper;

    public void add(PlayList playList) {
        LocalDateTime now = LocalDateTime.now();
        playList.setCreateTime(now);
        playListMapper.insert(playList);
    }

    public List<PlayList> list() {
        return playListMapper.selectAll();
    }
}
