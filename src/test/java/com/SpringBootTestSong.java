package com;

import com.dao.SongDaoImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@org.springframework.boot.test.context.SpringBootTest
public class SpringBootTestSong {

    @Autowired
    private SongDaoImpl songDao;

    @Test
    public void testQueryAll(){
        System.out.println(songDao.queryAll());
    }

}
