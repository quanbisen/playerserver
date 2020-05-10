package com.dao;

import com.pojo.Group;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author super lollipop
 * @date 20-2-18
 */
@Repository
public interface GroupDao {
    int insert(Group group);
    List<Group> queryByUserID(String userID);
    Group queryByID(int id);
    int delete(Group group);
    int update(Group group);
}
