package com.example.mapper;

import com.example.entity.RecentlyPlayed;
import com.example.vo.MusicVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecentlyPlayedMapper {

    @Insert("insert into `recently_played` (user_id,music_id,play_time) values (#{userId},#{musicId},#{playTime})")
    void insert(RecentlyPlayed recentlyPlayed);

    List<MusicVo> selectByUserId(Integer userId);
}
