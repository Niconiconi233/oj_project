package com.yw.ojproject.dto;

import com.yw.ojproject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @program: ojproject
*
* @description: 用户缩略信息
*
* @author: YW
*
* @create: 2020-03-19 15:04
**/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSimpleDto {
    public UserSimpleDto(User user)
    {
        this.id = user.getId();
        this.username = user.getUsername();
        this.real_name = user.getUsername();
    }
    private Integer id;
    private String username;
    private String real_name;
}
