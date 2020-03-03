package com.controller;

import com.dao.SingerDaoImpl;
import com.pojo.Singer;
import com.util.CutUtils;
import com.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
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

    @Value(value = "${server.hostname}")
    private  String hostname; //hostname,即http://114.116.240.232:8080/server，见SpringBoot的配置文件

    @GetMapping("/queryAll")
    @ResponseBody
    public List<Singer> queryAll(HttpServletRequest request){
        System.out.println(request.getSession().getId());
        LOGGER.info("queryAll");
        return singerDao.queryAll();
    }


    @PostMapping("/insert")
    @ResponseBody
    public String insert(
            @RequestParam("name") String name,
            @RequestParam("bytes") MultipartFile imageFile,
            HttpServletRequest request){
        if (name == null || imageFile == null){
            return "fail";
        }
        File filePath = new File(request.getServletContext().getRealPath("/")+ File.separator + "static" + File.separator + "image");  //图片资源存放路径
        if (!filePath.exists()){    //如果文件目录不存在，创建目录
            filePath.mkdirs();
        }

        String imageName = imageFile.getOriginalFilename();
        String imageExtendName = imageName.substring(imageName.lastIndexOf("."));
        System.out.println(filePath + File.separator + TimeUtils.getNowTimeFormatString() + imageExtendName);
        File imageDestination = new File(filePath + File.separator + TimeUtils.getNowTimeFormatString() + imageExtendName);
        try {
            imageFile.transferTo(imageDestination);
            LOGGER.info("上传歌手图片成功");
            String imagePrefix = CutUtils.cut(imageDestination.toString(),3);
            Singer singer = new Singer();
            String imageURL = imageDestination.toString().replaceAll(imagePrefix,hostname);
            singer.setImageURL(imageURL);
            singer.setName(name);

            String birthdayString = request.getParameter("birthday");
            String height = request.getParameter("height");
            String weight = request.getParameter("weight");
            String constellation = request.getParameter("constellation");
            String description = request.getParameter("description");

            if (birthdayString != null && !birthdayString.equals("")){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date birthday = simpleDateFormat.parse(birthdayString);
                singer.setBirthday(birthday);
            }
            if (height != null && !height.equals("")){
                singer.setHeight(Float.parseFloat(height));
            }
            if (weight != null && !weight.equals("")){
                singer.setWeight(Float.parseFloat(weight));
            }
            if (constellation != null && !constellation.equals("")){
                singer.setConstellation(constellation);
            }
            if (description != null && !constellation.equals("")){
                singer.setDescription(description);
            }
            int row = singerDao.insert(singer);
            if (row == 1){
                return "success";
            }else {
                return "fail";
            }

        }catch (IOException | ParseException e){
            e.printStackTrace();
        }
        return "fail";
    }



    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable int id, HttpServletRequest request){
        LOGGER.info("delete");
        Singer singer = singerDao.queryByID(id);  //查询出来，然后执行删除资源文件
        String staticRoot = request.getServletContext().getRealPath("/")+ File.separator + "static" + File.separator ;
        String resource = singer.getImageURL();
        File songFile = new File( staticRoot + "image" + resource.substring(resource.lastIndexOf("/")));
        if (songFile.exists()){
            songFile.delete();
            LOGGER.info("删除图片文件成功");
        }else {
            LOGGER.info("没有找到图片资源文件");
        }
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
    public String update(
            @RequestParam("id") String id,
            @RequestParam("name") String name,
            HttpServletRequest request) throws ParseException {

        Singer singer = new Singer();
        singer.setId(Integer.parseInt(id));
        singer.setName(name);

        String birthdayString = request.getParameter("birthday");
        String height = request.getParameter("height");
        String weight = request.getParameter("weight");
        String constellation = request.getParameter("constellation");
        String description = request.getParameter("description");

        if (birthdayString != null && !birthdayString.equals("")){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date birthday = simpleDateFormat.parse(birthdayString);
            singer.setBirthday(birthday);
        }else {
            singer.setBirthday(null);
        }
        if (height != null && !height.equals("")){
            singer.setHeight(Float.parseFloat(height));
        }else {
            singer.setHeight(0);
        }
        if (weight != null && !weight.equals("")){
            singer.setWeight(Float.parseFloat(weight));
        }else {
            singer.setHeight(0);
        }
        if (constellation != null && !constellation.equals("")){
            singer.setConstellation(constellation);
        }else {
            singer.setConstellation(null);
        }
        if (description != null && !constellation.equals("")){
            singer.setDescription(description);
        }else {
            singer.setDescription(null);
        }
        //开始更新数据操作
        int row = singerDao.update(singer);
        if (row == 1){
            return "success";
        }else {
            return "fail";
        }
    }
}
