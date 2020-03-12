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

    @Autowired
    private SongDaoImpl songDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(SongController.class);

    @Value(value = "${server.hostname}")
    private  String hostname; //hostname,即http://114.116.240.232:8080/server，见SpringBoot的配置文件

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

    @PostMapping("/upload")
    @ResponseBody
    public String upload(
            @RequestParam("songFile") MultipartFile songFile,
            @RequestParam("name") String name,
            @RequestParam("singer") String singer,
            @RequestParam("album") String album,
            @RequestParam("totalTime") String totalTime,
            @RequestParam("size") String size,
            @RequestParam("lyricFile") MultipartFile lyricFile,
            @RequestParam("bytes") MultipartFile albumFile,
            HttpServletRequest request){
        if (songFile.isEmpty() || lyricFile.isEmpty() || albumFile.isEmpty()) {
            return "fail";
        }
        String staticRoot = request.getServletContext().getRealPath("/")+ File.separator + "static";    //静态资源存放路径
        /**如果存储的目录不存在就创建目录*/
        File songPath = new File(staticRoot + File.separator + "song");
        if (!songPath.exists()){
            songPath.mkdirs();
            System.out.println(songPath.getPath());
        }
        File albumPath = new File(staticRoot + File.separator + "image");
        if (!albumPath.exists()){
            albumPath.mkdirs();
        }
        File lyricPath = new File(staticRoot + File.separator + "lyric");
        if (!lyricPath.exists()){
            lyricPath.mkdirs();
        }
        //拼接子路径
        String times = TimeUtils.getNowTimeFormatString();
        String songFileName = songFile.getOriginalFilename();
        String songExtendName = songFileName.substring(songFileName.lastIndexOf("."));
        String lyricFileName = lyricFile.getOriginalFilename();
        String lyricExtendName = lyricFileName.substring(lyricFileName.lastIndexOf("."));
        String albumFileName = albumFile.getOriginalFilename();
        String albumExtendName = albumFileName.substring(albumFileName.lastIndexOf("."));
        File songDestination = new File( staticRoot + File.separator + "song" + File.separator + times + songExtendName);
        File lyricDestination = new File(staticRoot + File.separator + "lyric" + File.separator + times + lyricExtendName);
        File albumDestination = new File(staticRoot + File.separator + "image" + File.separator + times + albumExtendName);
        try {
            songFile.transferTo(songDestination);
            LOGGER.info("上传歌曲文件成功");
            lyricFile.transferTo(lyricDestination);
            LOGGER.info("上传歌词文件成功");
            albumFile.transferTo(albumDestination);
            LOGGER.info("上传专辑文件成功");
            String songPathPrefix = CutUtils.cut(songDestination.toString(),3);
            System.out.println(songPathPrefix);
            String resourceURL = songDestination.toString().replaceAll(songPathPrefix,hostname);
            String lyricURL = lyricDestination.toString().replaceAll(songPathPrefix,hostname);  //因为歌词存储的目录和歌曲存储的目录在同一级下，所以可以使用歌曲存储的目录前缀来替换
            String albumURL = albumDestination.toString().replaceAll(songPathPrefix,hostname);
            Song song = new Song();
            song.setName(name);
            song.setAlbum(album);
            song.setSinger(singer);
            song.setSize(size);
            song.setTotalTime(totalTime);
            song.setResourceURL(resourceURL);
            song.setLyricURL(lyricURL);
            song.setAlbumURL(albumURL);
            int row = songDao.insert(song);
            if (row == 1){
                return "success";
            }
        } catch (IOException e) {
            LOGGER.error(e.toString(), e);
        }

        return "fail";
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
        Song song = new Song();
        song.setId(id);
        song.setName(name);
        song.setSinger(singer);
        song.setAlbum(album);
        int row = songDao.update(song);
        if (row == 1){
            LOGGER.info("update success.");
            return "success";
        }else {
            LOGGER.info("update fail.");
            return "fail";
        }
    }
}
