package com.dao;

import com.pojo.GroupSong;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author super lollipop
 * @date 20-3-9
 */
@Repository
public class GroupSongDaoImpl implements GroupSongDao {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public int insert(GroupSong groupSong) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int row = sqlSession.insert("com.pojo.GroupSongMapper.insert",groupSong);
        sqlSession.close();
        return row;
    }

    @Override
    public GroupSong query(GroupSong groupSong) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        groupSong = sqlSession.selectOne("com.pojo.GroupSongMapper.select",groupSong);
        sqlSession.close();
        return groupSong;
    }


}
