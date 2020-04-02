package com.yw.ojproject.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-14 16:47
**/
@Entity
@Data
@Table(name = "ProblemTag")
public class ProblemTag {

    @Id
    @GeneratedValue(generator = "guidGenerator")
    @GenericGenerator(name = "guidGenerator", strategy ="uuid")
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    private String id;

    @Column(name = "NAME")
    private String name;
}
