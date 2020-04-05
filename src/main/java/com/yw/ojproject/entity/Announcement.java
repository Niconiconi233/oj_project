package com.yw.ojproject.entity;

import com.yw.ojproject.bo.AnnouncementBo;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

/**
* @program: ojproject
*
* @description: 公告
*
* @author: YW
*
* @create: 2020-03-13 19:04
**/
@Entity
@Data
@Table(name = "Announcement")
public class Announcement {
    public Announcement(){}

    public Announcement(AnnouncementBo announcementBo)
    {
        this.title = announcementBo.getTitle();
        this.content = announcementBo.getContent();
        this.visible = announcementBo.getVisible();
        this.create_time = new Date();
        this.last_update_time = new Date();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "CREATE_TIME")
    private Date create_time;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "CREATE_BY", referencedColumnName = "ID")
    private User create_by;

    @Column(name = "LAST_UPDATE_TIME")
    private Date last_update_time;

    @Column(name = "VISIBLE")
    private Boolean visible;

    @Transient
    public void updateAnnouncement(AnnouncementBo announcementBo)
    {
        this.last_update_time = new Date();
        this.title = announcementBo.getTitle();
        this.content = announcementBo.getContent();
        this.visible = announcementBo.getVisible();
    }
}
