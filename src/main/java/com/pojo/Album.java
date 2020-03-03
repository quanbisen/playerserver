package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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
    private String imageURL;
}
