package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dao.SingerDaoImpl;
import com.pojo.Singer;
import com.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author super lollipop
 * @date 20-2-24
 */
@Controller
@RequestMapping("/singer")
public class SingerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingerController.class);

    @Autowired
    private SingerDaoImpl singerDao;

    @GetMapping("/queryAll")
    @ResponseBody
    public List<Singer> queryAll(HttpServletRequest request){
        LOGGER.info("queryAll");
        return singerDao.queryAll();
    }


    @PostMapping("/insert")
    @ResponseBody
    public String insert(@RequestParam("singer") String singerString,@RequestParam("bytes") MultipartFile multipartFile,HttpServletRequest request){
        if (singerString == null || multipartFile == null){
            return "fail";
        }
        Singer singer = JSONObject.parseObject(singerString,Singer.class);
        String imageURL = FileUtils.upload(multipartFile,request);
        if (!StringUtils.isEmpty(imageURL)){
            singer.setImageURL(imageURL);
        }else {
            return "fail";
        }
        int row = singerDao.insert(singer);
        if (row == 1){
            return "success";
        }else {
            return "fail";
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable int id, HttpServletRequest request){
        LOGGER.info("delete");
        Singer singer = singerDao.queryByID(id);  //查询出来，然后执行删除资源文件
        FileUtils.delete(singer.getImageURL(),request);
        int row = singerDao.deleteByID(id);
        return String.valueOf(row);
    }

    @GetMapping("/query/{string}")
    @ResponseBody
    public List<Singer> queryByNameLike(@PathVariable String string){
        LOGGER.info("queryByNameLike");
        return singerDao.queryByNameLike(string);
    }

    @GetMapping("/queryByName/{string}")
    @ResponseBody
    public List<Singer> queryByName(@PathVariable String string){
        LOGGER.info("queryByName");
        return singerDao.queryByName(string);
    }

    @PostMapping("/update")
    @ResponseBody
    public String update(@RequestParam("singer") String singerString){
        Singer singer = JSONObject.parseObject(singerString,Singer.class);
        //开始更新数据操作
        int row = singerDao.update(singer);
        if (row == 1){
            return "success";
        }else {
            return "fail";
        }
    }
}
