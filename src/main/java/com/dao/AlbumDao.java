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
    void insert(Album album);
    Album queryAlbumByName(String name);
    Map<String,String> queryAlbumMap(List<String> albumList);
}
