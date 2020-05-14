package com.controller;

import com.dao.SongDaoImpl;
import com.pojo.Group;
import com.util.CutUtils;
import com.util.TimeUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.pojo.Song;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * @author super lollipop
 * @date 20-2-22
 */
@Controller
@RequestMapping("/song")
public class SongController {

    private SongDaoImpl songDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(SongController.class);

    @Autowired
    public void constructor(SongDaoImpl songDao){
        this.songDao = songDao;
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
    public String insert(HttpServletRequest request){
        return "";
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable int id, HttpServletRequest request) throws FileNotFoundException {
        Song song = songDao.queryByID(id);  //查询出来，然后执行删除资源文件
        String staticRoot = request.getServletContext().getRealPath("/")+ File.separator + "static" + File.separator ;
        String resource = song.getResourceURL();
        File songFile = new File( staticRoot + "song" + resource.substring(resource.lastIndexOf("/")));
        if (songFile.exists()){
            songFile.delete();
            LOGGER.info("删除歌曲文件成功");
        }else {
            LOGGER.info("没有找到歌曲资源文件");
        }
        String lyric = song.getLyricURL();
        File lyricFile = new File(  staticRoot + "lyric" + lyric.substring(lyric.lastIndexOf("/")));
        if (lyricFile.exists()){
            lyricFile.delete();
            LOGGER.info("删除歌词文件成功");
        }else {
            LOGGER.info("没有找到歌词资源文件");
        }
        String albumURL = song.getAlbumURL();
        File albumFile = new File(staticRoot + "image" +  albumURL.substring(albumURL.lastIndexOf("/")));
        if (albumFile.exists()){
            albumFile.delete();
            LOGGER.info("删除专辑图片成功");
        }else {
            LOGGER.info("没有找到专辑图片资源文件");
        }
        int row = songDao.deleteByID(id);
        return String.valueOf(row);
    }

    @GetMapping("/query/{string}")
    @ResponseBody
    public List<Song> queryByName(@PathVariable String string){
        System.out.println(string);
        return songDao.queryByNameSingerAlbumLike(string);
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
