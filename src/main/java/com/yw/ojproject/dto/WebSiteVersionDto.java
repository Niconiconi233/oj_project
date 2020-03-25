package com.yw.ojproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-25 12:27
**/
@Data
@AllArgsConstructor
public class WebSiteVersionDto {
    public WebSiteVersionDto()
    {
        update = new LinkedList<>();
        update.add(new UpdateInfoDto());
    }
    private List<UpdateInfoDto> update;
    private String local_version = "2019-03-02";
}
