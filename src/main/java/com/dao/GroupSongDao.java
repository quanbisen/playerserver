package com.dao;

import com.pojo.GroupSong;
import org.springframework.stereotype.Repository;

/**
 * @author super lollipop
 * @date 20-3-9
 */
@Repository
public interface GroupSongDao {
    int insert(GroupSong groupSong);
    GroupSong query(GroupSong groupSong);
}
