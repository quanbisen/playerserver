package com.controller;

import com.alibaba.fastjson.JSON;
import com.dao.GroupDaoImpl;
import com.dao.UserDaoImpl;
import com.pojo.Group;
import com.pojo.User;
import com.redis.RedisService;
import com.response.RegisterResponse;
import com.util.*;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author super lollipop
 * @date 20-2-25
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Value(value = "${server.hostname}")
    private  String hostname; //hostname,见SpringBoot的配置文件

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserDaoImpl userDao;

    @Autowired
    private GroupDaoImpl groupDao;

    @PostMapping("/sendAuthenticationCode")
    @ResponseBody
    public RegisterResponse sendAuthenticationCode(
            @RequestParam String email,
            @RequestParam String password) throws IOException {
        //先验证账户是否已经注册了
        User user = userDao.queryById(email);
        RegisterResponse registerResponse = new RegisterResponse();
        if (user != null){
            registerResponse.setMessage("用户已注册");
            return registerResponse;
        }else if (redisService.get("code_"+email) != null){   //上一次注册存在验证码，需要等待
            redisService.remove("password_"+email); //移除上次存储的密码
            registerResponse.setMessage("操作太快");
            return registerResponse;
        }else {
            String code = EmailUtils.generateCode();
            redisService.set("code_"+email,code);
            redisService.expire("code_"+email,60);
            redisService.set("password_"+email,password);
            EmailUtils.sendEmail(email,code);   //发送邮箱验证码
            registerResponse.setId(email);
            registerResponse.setPassword(password);
            registerResponse.setCode(code);
            registerResponse.setMessage("验证码发送成功");
            registerResponse.setExpireSecond(60);   //60秒过期
            return registerResponse;
        }

    }

    @PostMapping("/register")
    @ResponseBody
    public RegisterResponse register(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String code,
            HttpServletRequest request) {

        RegisterResponse registerResponse = new RegisterResponse();
        String rightCode = redisService.get("code_"+email);
        String rightPassword = redisService.get("password_"+email);
        if (rightCode == null){
            registerResponse.setMessage("验证码已过期");
        }else if (code.equals(rightCode) && password.equals(rightPassword)){
            try {
                //创建用户，插入到用户表
                User user = new User();
                user.setId(email);
                user.setPassword(password);
                user.setName(MD5Utils.getMD5(email).substring(0,6));    //提取用户注册ID的MD5值的前6位作为用户名
                String localPath = request.getServletContext().getRealPath("/") + File.separator + "image" + File.separator + "UserDefaultImage.png";    //静态资源存放路径
                String localPathPrefix = CutUtils.cut(localPath,3);
                String imageURL = localPath.replaceAll(localPathPrefix,hostname);
                user.setImageURL(imageURL);
                int row = userDao.insert(user);
                //为用户创建"我喜欢的音乐"歌单
                Group group = new Group();
                group.setUserID(email);
                group.setName("我喜欢的音乐");
                group.setCreateTime(new Date());
                group.setFavor(1);  //1表示是"我喜欢的音乐"标记
                groupDao.insert(group);
                //删除注册时存储的持久化数据
                redisService.remove("code_"+email);
                redisService.remove("password_"+email);
                if (row == 1){
                    registerResponse.setMessage("注册成功");
                }
            }catch (Exception e){
                registerResponse.setMessage("注册失败");
            }

        }else {
            registerResponse.setMessage("验证错误");
        }
        return registerResponse;
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(HttpEntity<User> httpEntity){
        //获取请求的账号密码
        String id = httpEntity.getBody().getId();
        String password = httpEntity.getBody().getPassword();
        User user = new User();
        user.setId(id);
        user.setPassword(password);
        user = userDao.queryUserByIdAndPassword(user);  //查询数据库
        if (user != null){
            Date date = new Date();
            user.setLoginTime(date);
            //开始生成令牌
            Map<String,Object> map = new HashMap<>();
            map.put("id",user.getId());
            map.put("password",password);
            String token = JwtUtils.generateToken(map,date);
            user.setToken(token);
            user.setPassword(null); //清空密码字段
            userDao.updateUserByID(user);
            return JSON.toJSONString(user);
        }else {
            return "fail";
        }
    }

    @PostMapping("/validate")
    @ResponseBody
    public String validate(@RequestParam("token") String token){
        try {
            String id = JwtUtils.extractID(token);
            String password = JwtUtils.extractPassword(token);
            User user = new User();
            user.setId(id);
            user.setPassword(password);
            user = userDao.queryUserByIdAndPassword(user);
            if (user != null){
                user.setPassword(null);
                user.setToken(token);
                return JSON.toJSONString(user);
            }else {
                return "fail";
            }
        }catch (SignatureException signatureException){
            return "身份不合法";
        }catch (ExpiredJwtException expireJwtException){
            return "身份过期";
        }
    }

    @PostMapping("/update")
    @ResponseBody
    public User update(@RequestParam("user") String userString, MultipartHttpServletRequest multipartHttpServletRequest){
        User user = JSON.parseObject(userString,User.class);
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
                user.setImageURL(imageURL);    //设置图片资源URL
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        int row = userDao.updateUserByID(user);
        if (row == 1){
            user = userDao.queryById(user.getId());
            user.setPassword(null);
            return user;
        }
        return null;
    }

}
