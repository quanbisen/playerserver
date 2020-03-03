package com.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.pojo.Song;

import java.util.List;

/**
 * @author super lollipop
 * @date 20-2-22
 */
@Repository
public class SongDaoImpl implements SongDao{

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public int insert(Song song) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int row = sqlSession.insert("com.pojo.SongMapper.insert",song);
        sqlSession.close();
        return row;
    }

    @Override
    public Song queryByID(int id) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Song song = sqlSession.selectOne("com.pojo.SongMapper.queryById",id);
        sqlSession.close();
        return song;
    }

    @Override
    public int deleteByID(int id) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int row = sqlSession.delete("com.pojo.SongMapper.deleteById",id);
        sqlSession.close();
        return row;
    }

    @Override
    public int update(Song song) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int row = sqlSession.update("com.pojo.SongMapper.updateById",song);
        sqlSession.close();
        return row;
    }

    @Override
    public List<Song> queryAll() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Song> songList = sqlSession.selectList("com.pojo.SongMapper.queryAll");
        sqlSession.close();
        return songList;
    }

    @Override
    public List<Song> queryByNameSingerAlbum(String string) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Song> songList = sqlSession.selectList("com.pojo.SongMapper.queryByNameSingerAlbum",string);
        sqlSession.close();
        return songList;
    }

    @Override
    public Song queryLyric(Song song) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Song> songList = sqlSession.selectList("com.pojo.SongMapper.queryLyric",song);
        sqlSession.close();
        if (songList != null && songList.size() > 0){    //如果有多条记录，返回匹配的第一行数据
            return songList.get(0); //
        }else {
            return null;
        }
    }


}
