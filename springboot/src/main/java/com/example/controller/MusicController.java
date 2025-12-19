package com.example.controller;

import com.example.common.Result;
import com.example.entity.Music;
import com.example.service.MusicService;
import com.example.vo.MusicVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/music")
public class MusicController {

    @Resource
    public MusicService musicService;

    /**
     * 歌曲上传
     */
    @PostMapping("/upload")
    public Result upload(@RequestBody MultipartFile file){
        musicService.upload(file);
        return Result.success();
    }

    /**
     * 查询所有歌曲
     * required=false表示该变量非必须，没有就设置为null，默认情况下required为true
     */
    @GetMapping("/selectAll")
    public Result selectAll(@RequestAttribute(value="userId", required=false) Integer userId){
        if (userId!=null){
            List<MusicVo> musicVoList=musicService.selectAllWithUserId(userId);
            return Result.success(musicVoList);
        }
        List<Music> musicList=musicService.selectAll();
        return Result.success(musicList);
    }

    /**
     * 更新音乐
     */
    @PutMapping("/update/{id}")
    public Result updateById(@PathVariable Integer id,@RequestBody MultipartFile file){
        musicService.updateById(id,file);
        return Result.success();
    }

}
