package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author super lollipop
 * @date 20-3-9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupSong {
    private int groupID;
    private int songID;
    private Date addTime;
}
