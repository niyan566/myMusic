package com.example.service;

import com.example.entity.RecentlyPlayed;
import com.example.mapper.RecentlyPlayedMapper;
import com.example.vo.MusicVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecentlyPlayedService {

    @Resource
    private RecentlyPlayedMapper playedMapper;

    public void record(RecentlyPlayed recentlyPlayed) {
        LocalDateTime now = LocalDateTime.now();

        recentlyPlayed.setPlayTime(now);
        playedMapper.insert(recentlyPlayed);
    }

    public List<MusicVo> listByUserId(Integer userId) {
        return playedMapper.selectByUserId(userId);
    }
}
