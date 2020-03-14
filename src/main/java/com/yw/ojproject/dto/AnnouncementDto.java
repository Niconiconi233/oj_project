package com.yw.ojproject.dto;

import com.yw.ojproject.entity.Announcement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
* @program: ojproject
*
* @description: 
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
        this.created_by = new userimpl(announcement.getCreate_by());
        this.title = announcement.getTitle();
        this.content = announcement.getContent();
        this.create_time = announcement.getCreate_time();
        this.last_update_time = announcement.getLast_update_time();
        this.visible = announcement.getVisible();
    }

    @Data
    @NoArgsConstructor
    class userimpl
    {

        public userimpl(String username)
        {
            this.username = username;
        }
        private String username;
    }

    private String id;
    private userimpl created_by;
    private String title;
    private String content;
    private Date create_time;
    private Date last_update_time;
    private Boolean visible;
}
