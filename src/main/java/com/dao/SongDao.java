package com.dao;

import org.springframework.stereotype.Repository;
import com.pojo.Song;

import java.util.List;

/**
 * @author super lollipop
 * @date 20-2-22
 */
@Repository
public interface SongDao {

    int insert(Song song);
    Song queryByID(int id);
    int deleteByID(int id);
    int update(Song song);
    List<Song> queryAll();
    List<Song> queryByNameSingerAlbum(String string);
    Song queryLyric(Song song);
}
