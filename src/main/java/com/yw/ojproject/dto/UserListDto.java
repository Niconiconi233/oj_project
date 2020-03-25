package com.yw.ojproject.dto;

import com.yw.ojproject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.LinkedList;
import java.util.List;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-25 19:05
**/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListDto {
    public UserListDto(Page<User> page)
    {
        results = new LinkedList<>();
        for(User u : page.getContent())
        {
            results.add(new UserTotalDto(u));
        }
        totoal = page.getTotalElements();
    }

    private List<UserTotalDto> results;
    private Long totoal;
}
