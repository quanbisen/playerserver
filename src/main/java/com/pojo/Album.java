package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.Date;

/**
 * @author super lollipop
 * @date 20-2-24
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Album {
    private Integer id;
    private String name;
    private Integer singerID;
    private String singerName;
    private Date publishTime;
    private String description;
    private String imageURL;
}
