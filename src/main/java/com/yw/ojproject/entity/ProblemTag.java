package com.yw.ojproject.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

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

    @JSONField(serialize = false)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "tags", targetEntity = Problem.class)
    private List<Problem> problems;
}
