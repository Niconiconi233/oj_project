package com.yw.ojproject.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-25 21:08
**/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementBo {
    private Long id;
    private String title;
    private String content;
    private Boolean visible;
}
