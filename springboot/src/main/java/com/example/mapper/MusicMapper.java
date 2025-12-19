package com.example.mapper;

import com.example.entity.Music;
import com.example.vo.MusicVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MusicMapper {

    void insert(Music music);

    List<Music> selectAll();

    void updateById(Music music);

    List<MusicVo> selectAllWithUserId(Integer userId);
}
