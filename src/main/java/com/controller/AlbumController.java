package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dao.AlbumDaoImpl;
import com.pojo.Album;
import com.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author super lollipop
 * @date 20-5-12
 */
@Controller
@RequestMapping("/album")
public class AlbumController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlbumController.class);

    private AlbumDaoImpl albumDao;

    @Autowired
    public void constructor(AlbumDaoImpl albumDao){
        this.albumDao = albumDao;
    }

    @GetMapping("/queryAll")
    @ResponseBody
    public List<Album> queryAll(){
        LOGGER.info("queryAll");
        return albumDao.queryAll();
    }

    @PostMapping("/queryByName")
    @ResponseBody
    public List<Album> queryByName(@RequestParam("name") String name){
        return albumDao.queryByName(name);
    }

    @PostMapping("queryByNameLike")
    @ResponseBody
    public List<Album> queryByNameLike(@RequestParam("name") String name){
        return albumDao.queryByNameLike(name);
    }

    @PostMapping("/insert")
    @ResponseBody
    public String insert(@RequestParam("album") String albumString, @RequestParam("bytes") MultipartFile multipartFile, HttpServletRequest request){
        Album album = JSONObject.parseObject(albumString,Album.class);
        /**上传文件*/
        String imageURL = FileUtils.upload(multipartFile,request);
        album.setImageURL(imageURL);
        int row = albumDao.insert(album);
        if (row == 1){
            return "success";
        }else {
            return "fail";
        }
    }

    @PostMapping("update")
    @ResponseBody
    public String update(@RequestParam("album") String albumString){
        Album album = JSONObject.parseObject(albumString,Album.class);
        int row = albumDao.update(album);
        if (row == 1){
            return "success";
        }else {
            return "fail";
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String DeleteByID(@PathVariable("id") int id){
        int row = albumDao.deleteByID(id);
        if (row == 1){
            return "success";
        }else {
            return "fail";
        }
    }
}
