package com.controller;

import com.alibaba.fastjson.JSON;
import com.dao.GroupDaoImpl;
import com.pojo.Group;
import com.util.CutUtils;
import com.util.JwtUtils;
import com.util.TimeUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author super lollipop
 * @date 20-2-26
 */
@Controller
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupDaoImpl groupDao;

    @Value(value = "${server.hostname}")
    private  String hostname; //hostname,即http://114.116.240.232:8080/server，见SpringBoot的配置文件

    @GetMapping("/query/{token}")
    @ResponseBody
    public String queryByUser(@PathVariable String token){
        try {
            String id = JwtUtils.extractID(token);
            if (id != null && !id.equals("")){
                List<Group> groupList = groupDao.queryByUserID(id);
                return JSON.toJSONString(groupList);
            }
        }catch (SignatureException signatureException){
            return "身份不合法";
        }catch (ExpiredJwtException expiredJwtException){
            return "身份过期";
        }
        return null;
    }

    @PostMapping("/insert")
    @ResponseBody
    public String insert(
            @RequestParam("name") String name,
            @RequestParam("token") String token){
        try {
            String id = JwtUtils.extractID(token);
            if (id != null && !id.equals("")){
                Group group = new Group();
                group.setName(name);
                group.setUserID(id);
                group.setFavor(0);  //0代表用户自己创建的歌单
                int row = groupDao.insert(group);
                if (row == 1){
                    return JSON.toJSONString(group);
                }else {
                    return "fail";
                }
            }
        }catch (SignatureException signatureException){
            return "身份不合法";
        }catch (ExpiredJwtException expiredJwtException){
            return "身份过期";
        }
        return null;
    }

    @PostMapping("/delete")
    @ResponseBody
    public String delete(HttpEntity<Group> groupHttpEntity){
        Group group = groupHttpEntity.getBody();
        int row = groupDao.delete(group);
        if (row == 1){
            return "删除歌单成功";
        }else {
            return "删除歌单失败";
        }
    }

    @PostMapping("/update")
    @ResponseBody
    public String update(@RequestParam("group") String groupString, MultipartHttpServletRequest multipartHttpServletRequest){
        Group group = JSON.parseObject(groupString,Group.class);
        MultipartFile imageMultipartFile = multipartHttpServletRequest.getFile("image");
        if (imageMultipartFile != null){ //如果上传的更新信息存在图片,保存图片
            String staticRoot = multipartHttpServletRequest.getServletContext().getRealPath("/")+ File.separator + "static";    //静态资源存放路径
            /**如果存储的目录不存在就创建目录*/
            File imagePath = new File(staticRoot + File.separator + "image");
            if (!imagePath.exists()){
                imagePath.mkdirs();
            }
            //拼接子路径
            String times = TimeUtils.getNowTimeFormatString();
            String imageFileName = imageMultipartFile.getOriginalFilename();
            String imageExtendName = imageFileName.substring(imageFileName.lastIndexOf("."));
            File imageDestination = new File(staticRoot + File.separator + "image" + File.separator + times + imageExtendName);
            try {
                imageMultipartFile.transferTo(imageDestination);
                System.out.println("上传文件-" + imageFileName + "-成功");
                String imagePathPrefix = CutUtils.cut(imageDestination.toString(),3);
                System.out.println(imagePathPrefix);
                String imageURL = imageDestination.toString().replaceAll(imagePathPrefix,hostname);
                group.setImageURL(imageURL);    //设置图片资源URL
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        int row = groupDao.update(group);
        if (row == 1){
            return "保存成功";
        }else {
            return "保存失败";
        }

    };
}
