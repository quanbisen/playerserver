package com.dao;

import org.springframework.stereotype.Repository;
import com.pojo.Singer;

import java.util.List;

/**
 * @author super lollipop
 * @date 20-2-8
 */
@Repository
public interface SingerDao {
    List<Singer> queryAll();
    List<Singer> queryByName(String name);
    Singer queryByID(int id);
    List<Singer> queryByNameLike(String name);
    int insert(Singer singer);
    int update(Singer singer);
    int deleteByID(int id);
}
