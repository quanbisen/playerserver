package com.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;
import com.pojo.Singer;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author super lollipop
 * @date 20-2-8
 */
@Repository(value = "singerDao")
public class SingerDaoImpl implements SingerDao{

    /**注入MyBatis的SqlSessionFactory对象*/
    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public List<Singer> queryAll() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Singer> singerList = sqlSession.selectList("com.pojo.SingerMapper.queryAll");
        sqlSession.close();
        return singerList;
    }

    /**根据名称查找数据库的歌手记录
     * @param name 歌手名称
     * @return Singer 对象*/
    @Override
    public List<Singer> queryByName(String name) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Singer> singerList = sqlSession.selectList("com.pojo.SingerMapper.queryByName",name);
        sqlSession.close();
        return singerList;
    }

    @Override
    public Singer queryByID(int id) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Singer singer = sqlSession.selectOne("com.pojo.SingerMapper.queryById",id);
        sqlSession.close();
        return singer;
    }

    @Override
    public List<Singer> queryByNameLike(String name) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Singer> singerList = sqlSession.selectList("com.pojo.SingerMapper.queryByNameLike",name);
        sqlSession.close();
        return singerList;
    }


    @Override
    public int insert(Singer singer) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int row = sqlSession.insert("com.pojo.SingerMapper.insert",singer);
        sqlSession.close();
        return row;
    }

    @Override
    public int update(Singer singer) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int row = sqlSession.update("com.pojo.SingerMapper.updateById",singer);
        sqlSession.close();
        return row;
    }

    @Override
    public int deleteByID(int id) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int row = sqlSession.delete("com.pojo.SingerMapper.deleteById",id);
        sqlSession.close();
        return row;
    }

}
