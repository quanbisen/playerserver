package com.controller;

import com.alibaba.fastjson.JSON;
import com.dao.GroupSongDaoImpl;
import com.dao.SongDaoImpl;
import com.pojo.Group;
import com.pojo.GroupSong;
import com.pojo.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Date;

/**
 * @author super lollipop
 * @date 20-3-9
 */
@Controller
@RequestMapping("/groupSong")
public class GroupSongController {

    @Autowired
    private SongDaoImpl songDao;

    @Autowired
    private GroupSongDaoImpl groupSongDao;

    @PostMapping("/insert")
    @ResponseBody
    public String insert(@RequestParam("song") String songStr, @RequestParam("group") String groupStr){
        Song song = JSON.parseObject(songStr,Song.class);
        Group group = JSON.parseObject(groupStr,Group.class);
        song = songDao.queryByNameSingerAlbum(song);
        if (song != null){
            GroupSong groupSong = new GroupSong(group.getId(),song.getId(),new Date());
            if (groupSongDao.query(groupSong) == null){ //如果查询无此条记录,才进行插入
                int row = groupSongDao.insert(groupSong);
                if (row == 1){
                    return "添加成功";
                }else {
                    return "添加失败";
                }
            }else { //否则,查询到已存在记录,返回"歌曲已存在"
                return "歌曲已存在";
            }

        }else {
            return "无此歌曲资源";
        }
    }
}
