package com.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;
import com.pojo.Group;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author super lollipop
 * @date 20-2-18
 */
@Repository
public class GroupDaoImpl implements GroupDao{

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public int insert(Group group) {
        try {
            SqlSession sqlSession = sqlSessionFactory.openSession();
            int row = sqlSession.insert("com.pojo.GroupMapper.insert",group);
            sqlSession.close();
            return row;
        }catch (Exception e){e.printStackTrace();}
        return 0;
    }

    @Override
    public List<Group> queryByUserID(String userID) {
        try {
            SqlSession sqlSession = sqlSessionFactory.openSession();
            List<Group> groupList = sqlSession.selectList("com.pojo.GroupMapper.queryByUserId",userID);
            sqlSession.close();
            return groupList;
        }catch (Exception e){e.printStackTrace();}
        return null;
    }

    @Override
    public Group queryByID(int id) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Group group = sqlSession.selectOne("com.pojo.GroupMapper.queryById",id);
        sqlSession.close();
        return group;
    }

    @Override
    public int delete(Group group) {
        try {
            SqlSession sqlSession = sqlSessionFactory.openSession();
            int row = sqlSession.delete("com.pojo.GroupMapper.delete",group);
            sqlSession.close();
            return row;
        }catch (Exception e){e.printStackTrace();}
        return 0;

    }

    @Override
    public int update(Group group) {
        try {
            SqlSession sqlSession = sqlSessionFactory.openSession();
            int row = sqlSession.update("com.pojo.GroupMapper.update",group);
            sqlSession.close();
            return row;
        }catch (Exception e){e.printStackTrace();}
        return 0;
    }

}
