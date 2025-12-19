package com.example.service;

import cn.hutool.jwt.JWT;
import com.example.entity.LikedMusic;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.LikedMusicMapper;
import com.example.mapper.UserMapper;
import com.example.vo.MusicVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserService {

    @Resource
    public UserMapper userMapper;

    @Resource
    public LikedMusicMapper likedMusicMapper;

    @Value("$jwt.secret")
    private String secret;

    public Map<String,Object> login(User user) {
        if (user.getUsername().isEmpty() || user.getPassword().isEmpty()){
            throw new CustomException("500","账号或密码为空");
        }


        User result=userMapper.selectByUsername(user.getUsername());
        if (result==null){
            throw new CustomException("500","此账号不存在");
        }

        if (!Objects.equals(result.getPassword(), user.getPassword())){
            throw new CustomException("500","密码不正确");
        }

        //创建token
        String token= JWT.create()
                .setPayload("iat", System.currentTimeMillis())//签发时间
                .setPayload("exp", System.currentTimeMillis()+24*60*60+1000)//过期时间
                .setPayload("id",result.getId())
                .setPayload("username",result.getUsername())
                .setKey(secret.getBytes())
                .sign();
        log.info(token);

        //将密码清空,不能返回给前端
        result.setPassword(null);

        Map<String,Object> map=new HashMap<>();
        map.put("token",token);
        map.put("user",result);
        return map;
    }

    public void addOrRemoveLikedMusic(LikedMusic likedMusic) {
        LikedMusic result = likedMusicMapper.selectOne(likedMusic);
        if (result!=null){ //先判断是否存在这条数据，存在就是移出我喜欢的音乐
            likedMusicMapper.remove(result);
        }else {            //不存在就是添加到我喜欢的音乐
            LocalDateTime now=LocalDateTime.now();
            likedMusic.setCreateTime(now);
            likedMusicMapper.insert(likedMusic);
        }
    }

    public List<MusicVo> listLikedMusic(Integer userId) {
        return likedMusicMapper.selectMusicByUserId(userId);
    }
}
