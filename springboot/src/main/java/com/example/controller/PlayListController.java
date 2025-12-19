package com.example.controller;

import com.example.common.Result;
import com.example.entity.PlayList;
import com.example.exception.CustomException;
import com.example.service.PlayListService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playList")
public class PlayListController {

    @Resource
    private PlayListService playListService;

    /**
     * 新增歌单
     */
    @PostMapping("/add")
    public Result add(@RequestBody PlayList playList,
                      @RequestAttribute(value = "userId",required = false) Integer userId){
        if (userId==null){
            throw new CustomException("500","系统错误");
        }
        playList.setUserId(userId);
        playListService.add(playList);
        return Result.success();
    }

    /**
     * 获取歌单列表
     */
    @GetMapping("/list")
    public Result list(){
        List<PlayList> playLists=playListService.list();
        return Result.success(playLists);
    }

}
