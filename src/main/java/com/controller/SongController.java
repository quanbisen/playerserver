package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dao.SingerSongDaoImpl;
import com.dao.SongDaoImpl;
import com.pojo.Singer;
import com.pojo.SingerSong;
import com.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.pojo.Song;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

/**
 * @author super lollipop
 * @date 20-2-22
 */
@Controller
@RequestMapping("/song")
public class SongController {

    private SongDaoImpl songDao;

    private SingerSongDaoImpl singerSongDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(SongController.class);

    @Autowired
    public void constructor(SongDaoImpl songDao, SingerSongDaoImpl singerSongDao){
        this.songDao = songDao;
        this.singerSongDao = singerSongDao;
    }

    @GetMapping("/queryAll")
    @ResponseBody
    public List<Song> queryAll(){
        return songDao.queryAll();
    }

    @PostMapping("/queryLyric")
    @ResponseBody
    public Song queryLyric(HttpEntity<Song> songHttpEntity){
        Song song = songHttpEntity.getBody();
        return songDao.queryLyric(song);
    }

    @PostMapping("/insert")
    @ResponseBody
    @Transactional
    public String insert(@RequestParam("song") String songString,@RequestParam("songBytes") MultipartFile songFile,
                         @RequestParam("lyricBytes") MultipartFile lyricFile,@RequestParam("albumBytes") MultipartFile albumFile, HttpServletRequest request){
        Song song = JSONObject.parseObject(songString,Song.class);
        Date nowDate = new Date();
        String songURL = FileUtils.upload(songFile,request);
        String lyricURL = FileUtils.upload(lyricFile,request);
        String albumURL = FileUtils.upload(albumFile,request);
        song.setPublishTime(nowDate);
        song.setResourceURL(songURL);
        song.setLyricURL(lyricURL);
        song.setAlbumURL(albumURL);
        if (song.getAlbumObject() != null){
            song.setAlbumID(song.getAlbumObject().getId());
            song.setCollectTime(nowDate);
        }
        int rowForSong = songDao.insert(song);
        List<Singer> singerList = song.getSingerList();
        int rowForSingerSong = 0;
        for (int i = 0; i < singerList.size(); i++) {
            SingerSong singerSong = new SingerSong(singerList.get(i).getId(),song.getId());
            int row = singerSongDao.insert(singerSong);
            if (row == 1){
                rowForSingerSong++;
            }
        }
        if (rowForSong == 1 && rowForSingerSong == singerList.size()){
            return "success";
        }else {
            return "fail";
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    @Transactional
    public String delete(@PathVariable int id, HttpServletRequest request) throws FileNotFoundException {
        Song song = songDao.queryByID(id);  //查询出来，然后执行删除资源文件
        FileUtils.delete(song.getResourceURL(),"song",request); //删除资源文件
        FileUtils.delete(song.getLyricURL(),"lyric",request);   //删除歌词文件
        FileUtils.delete(song.getAlbumURL(),"image",request);   //删除专辑图文件
        int rowForSingerSong = singerSongDao.deleteByID(song.getId());
        int rowForSong = songDao.deleteByID(id);
        if (rowForSingerSong > 0 && rowForSong > 0){
            return "success";
        }else {
            return "fail";
        }
    }

    @GetMapping("/query/{string}")
    @ResponseBody
    public List<Song> queryByName(@PathVariable String string){
        System.out.println(string);
        return songDao.queryByNameSingerAlbumLike(string);
    }

    @PostMapping("queryByNameLike")
    @ResponseBody
    public List<Song> queryByNameLike(@RequestParam("name") String name){
        return songDao.queryByNameLike(name);
    }

    @PostMapping("/update")
    @ResponseBody
    public String update(@RequestParam Integer id,@RequestParam String name,@RequestParam String singer,@RequestParam String album){
//        Song song = new Song();
//        song.setId(id);
//        song.setName(name);
//        song.setSinger(singer);
//        song.setAlbum(album);
//        int row = songDao.update(song);
//        if (row == 1){
//            LOGGER.info("update success.");
//            return "success";
//        }else {
//            LOGGER.info("update fail.");
//            return "fail";
//        }
        return "";
    }
}
