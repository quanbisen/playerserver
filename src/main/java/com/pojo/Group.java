package com.pojo;

import lombok.*;
import java.util.Date;

/**
 * @author super lollipop
 * @date 20-2-18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    private int id;
    private String name;
    private String description;
    private Date createTime;
    private String userID;
    private String imageURL;
    private int favor;
}
