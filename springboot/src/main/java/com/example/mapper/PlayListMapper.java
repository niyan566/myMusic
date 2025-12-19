package com.example.mapper;

import com.example.entity.PlayList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PlayListMapper {

    void insert(PlayList playList);

    @Select("select * from `play_list`")
    List<PlayList> selectAll();
}
