package com.dao;

import org.springframework.stereotype.Repository;
import com.pojo.User;

/**
 * @author super lollipop
 * @date 19-12-6
 */
@Repository
public interface UserDao {
    User queryUserByIdAndPassword(User user);
    User queryUserByIdToken(User user);
    int insert(User user);
    int updateUserByID(User user);
    User queryById(String id);
//    User queryUserAndGroup(String id);
}
