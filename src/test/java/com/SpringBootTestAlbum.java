package com;

import com.dao.AlbumDaoImpl;
import com.pojo.Album;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Date;

@RunWith(SpringRunner.class)
@org.springframework.boot.test.context.SpringBootTest
public class SpringBootTestAlbum {
    @Autowired
    private AlbumDaoImpl albumDao;

    @Test
    public void testInsert(){
        Album album = new Album();
        album.setName("美好的日子");
        album.setDescription("这是最好听的专辑");
        album.setPublishTime(new Date());
        album.setSingerID(8);
        int row = albumDao.insert(album);
        System.out.println(row);
    }

    @Test
    public void testQueryAll(){
        System.out.println(albumDao.queryAll());
    }

    @Test
    public void testQueryByName(){
        System.out.println(albumDao.queryByName("美好的日子"));
    }

    @Test
    public void testQueryByNameLike(){
        System.out.println(albumDao.queryByNameLike("日子"));
    }

    @Test
    public void testUpdate(){
        Album album = new Album();
        album.setId(1);
        album.setName("美好的日子");
        album.setDescription("这是最好听的专辑和我喜欢的专辑");
        album.setPublishTime(new Date());
//        album.setSingerID(8);
        int row = albumDao.update(album);
        System.out.println(row);
    }

    @Test
    public void testDelete(){
        System.out.println(albumDao.deleteByID(1));
    }
}
