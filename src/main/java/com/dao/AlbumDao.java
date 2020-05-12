package com.dao;

import com.pojo.Album;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author super lollipop
 * @date 20-2-22
 */
@Repository
public interface AlbumDao {
    List<Album> queryAll();
    int insert(Album album);
    List<Album> queryByName(String name);
    List<Album> queryByNameLike(String name);
    int deleteByID(int id);
    int update(Album album);
}
