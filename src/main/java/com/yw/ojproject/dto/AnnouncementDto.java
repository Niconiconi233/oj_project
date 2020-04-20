package com.yw.ojproject.dto;

import com.yw.ojproject.entity.Announcement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
* @program: ojproject
*
* @description: 公告DTO
*
* @author: YW
*
* @create: 2020-03-13 19:46
**/
@Data
@NoArgsConstructor
public class AnnouncementDto {

    public AnnouncementDto(Announcement announcement)
    {
        this.id = announcement.getId();
        this.created_by = new UserSimpleDto(announcement.getCreate_by());
        this.title = announcement.getTitle();
        this.content = announcement.getContent();
        this.create_time = announcement.getCreate_time();
        this.last_update_time = announcement.getLast_update_time();
        this.visible = announcement.getVisible();
    }

    private Long id;
    private UserSimpleDto created_by;
    private String title;
    private String content;
    private Date create_time;
    private Date last_update_time;
    private Boolean visible;
}
