package com.dao;

import com.pojo.SingerSong;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class SingerSongDaoImpl implements SingerSongDao{

    /**注入MyBatis的SqlSessionFactory对象*/
    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public int insert(SingerSong singerSong) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int row = sqlSession.insert("com.pojo.SingerSongMapper.insert",singerSong);
        sqlSession.close();
        return row;
    }

    @Override
    public int deleteByID(int songID) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int row = sqlSession.delete("com.pojo.SingerSongMapper.deleteById",songID);
        sqlSession.close();
        return row;
    }
}
