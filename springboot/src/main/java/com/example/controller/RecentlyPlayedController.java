package com.example.controller;

import com.example.common.Result;
import com.example.entity.RecentlyPlayed;
import com.example.service.RecentlyPlayedService;
import com.example.vo.MusicVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("recentlyPlayed")
public class RecentlyPlayedController {

    @Resource
    private RecentlyPlayedService playedService;


    @PostMapping("record")
    public Result record(@RequestBody RecentlyPlayed recentlyPlayed,
                         @RequestAttribute("userId") Integer userId){
        recentlyPlayed.setUserId(userId);
        playedService.record(recentlyPlayed);
        return Result.success();
    }


    @GetMapping("list")
    public Result listByUserId(@RequestAttribute("userId") Integer userId){
        List<MusicVo> list = playedService.listByUserId(userId);
        return Result.success(list);
    }
}
