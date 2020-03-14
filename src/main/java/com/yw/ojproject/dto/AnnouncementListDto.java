package com.yw.ojproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-14 14:41
**/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementListDto {
    private List<AnnouncementDto> results;
    private Integer total;
}
