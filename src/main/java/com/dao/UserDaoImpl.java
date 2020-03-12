package com.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;
import com.pojo.User;
import javax.annotation.Resource;

/**
 * @author super lollipop
 * @date 19-12-6
 */
@Repository
public class UserDaoImpl implements UserDao{

    /**注入MyBatis的SqlSessionFactory对象*/
    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public User queryUserByIdAndPassword(User user) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        user = sqlSession.selectOne("com.pojo.UserMapper.findUserByIdAndPassword",user);
        sqlSession.close();
        return user;
    }

    @Override
    public User queryUserByIdToken(User user) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User validUser = sqlSession.selectOne("com.pojo.UserMapper.findUserByIdToken",user);
        sqlSession.close();
        return validUser;
    }



    @Override
    public int updateLoginTime(User user) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int row = sqlSession.update("com.pojo.UserMapper.updateLoginTime",user);
        sqlSession.close();
        return row;
    }

    @Override
    public User queryById(String id) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = sqlSession.selectOne("com.pojo.UserMapper.queryById",id);
        sqlSession.close();
        return user;
    }

    @Override
    public User queryUserAndGroup(String id) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = sqlSession.selectOne("com.pojo.UserMapper.queryUserAndGroup",id);
        sqlSession.close();
        return user;
    }

    @Override
    public int insert(User user) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int row = sqlSession.insert("com.pojo.UserMapper.insert",user);
        sqlSession.close();
        return row;
    }


}
