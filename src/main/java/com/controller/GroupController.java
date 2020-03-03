package com.controller;

import com.alibaba.fastjson.JSON;
import com.dao.GroupDaoImpl;
import com.pojo.Group;
import com.util.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
}
