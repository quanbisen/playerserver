package com.dao;

import com.pojo.Album;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.*;

/**
 * @author super lollipop
 * @date 20-2-10
 */
@Repository
public class AlbumDaoImpl implements AlbumDao{


    private SqlSessionFactory sqlSessionFactory;

    /**注入MyBatis的SqlSessionFactory对象*/
    @Autowired
    public void constructor(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public List<Album> queryAll() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Album> albumList = sqlSession.selectList("com.pojo.AlbumMapper.queryAll");
        sqlSession.close();
        return albumList;
    }

    @Override
    public int insert(Album album) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int row = sqlSession.insert("com.pojo.AlbumMapper.insert",album);
        sqlSession.close();
        return row;
    }

    @Override
    public List<Album> queryByName(String name) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Album> albumList = sqlSession.selectList("com.pojo.AlbumMapper.queryByName",name);
        sqlSession.close();
        return albumList;
    }

    @Override
    public List<Album> queryByNameLike(String name) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Album> albumList = sqlSession.selectList("com.pojo.AlbumMapper.queryByNameLike",name);
        sqlSession.close();
        return albumList;
    }

    @Override
    public int deleteByID(int id) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int row = sqlSession.delete("com.pojo.AlbumMapper.deleteById",id);
        sqlSession.close();
        return row;
    }

    @Override
    public int update(Album album) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int row = sqlSession.update("com.pojo.AlbumMapper.update",album);
        sqlSession.close();
        return row;
    }

}
