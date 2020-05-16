package com.dao;

import com.pojo.SingerSong;
import org.springframework.stereotype.Repository;

@Repository
public interface SingerSongDao {
    int insert(SingerSong singerSong);
    int deleteByID(int songID);
}
