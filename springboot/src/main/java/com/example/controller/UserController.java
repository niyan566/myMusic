package com.example.controller;

import com.example.common.Result;
import com.example.entity.LikedMusic;
import com.example.entity.Music;
import com.example.entity.User;
import com.example.service.UserService;
import com.example.vo.MusicVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {


    @Resource
    public UserService userService;

    /**
     * 登录
     */
    @GetMapping("/login")
    public Result login(User user){
        Map<String,Object> userMap=userService.login(user);
        return Result.success(userMap);
    }

    /**
     * 添加喜欢音乐 / 将喜欢的音乐改为不喜欢
     */
    @PostMapping("/toLiked")
    public Result toLiked(@RequestBody LikedMusic likedMusic,@RequestAttribute("userId") Integer userid){ //Jackson只能将json反序列为对象
        likedMusic.setUserId(userid);
        userService.addOrRemoveLikedMusic(likedMusic);
        return Result.success();
    }

    /**
     * 查询喜欢的音乐
     */
    @GetMapping("/listLikedMusic")
    public Result listLikedMusic(@RequestAttribute("userId") Integer userId){
        List<MusicVo> musicList=userService.listLikedMusic(userId);
        log.info(musicList.toString());
        return Result.success(musicList);
    }
}
