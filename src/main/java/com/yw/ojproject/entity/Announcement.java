package com.yw.ojproject.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

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

    @Id
    @GeneratedValue(generator = "guidGenerator")
    @GenericGenerator(name = "guidGenerator", strategy ="uuid")
    @Column(name = "ID", unique = true, nullable = true, length = 32)
    private String id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "CREATE_TIME")
    private Date create_time;

    @Column(name = "CREATE_BY")
    private String create_by;

    @Column(name = "LAST_UPDATE_TIME")
    private Date last_update_time;

    @Column(name = "VISIBLE")
    private Boolean visible;
}
