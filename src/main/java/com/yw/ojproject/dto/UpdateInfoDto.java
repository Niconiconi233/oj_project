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
* @create: 2020-03-25 12:26
**/
@Data
@AllArgsConstructor
public class UpdateInfoDto {
    public UpdateInfoDto()
    {
        details.add("first build");
    }
    private String version = "2020-03-02";
    private String level = "Recommend";
    private String title = "2020-03-02";
    private List<String> details = new LinkedList<>();
}
