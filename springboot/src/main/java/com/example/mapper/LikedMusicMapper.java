package com.example.mapper;

import com.example.entity.LikedMusic;
import com.example.vo.MusicVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LikedMusicMapper {

    @Insert("insert into `liked_music` (user_id,music_id,create_time) values (#{userId},#{musicId},#{createTime})")
    void insert(LikedMusic likedMusic);

    List<MusicVo> selectMusicByUserId(Integer userId);

    @Select("select * from `liked_music` where user_id=#{userId} and music_id=#{musicId}")
    LikedMusic selectOne(LikedMusic likedMusic);

    @Delete("delete from `liked_music` where id=#{id}")
    void remove(LikedMusic likedMusic);
}
