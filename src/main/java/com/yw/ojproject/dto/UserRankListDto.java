package com.yw.ojproject.dto;

import com.yw.ojproject.entity.UserProfile;
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
* @create: 2020-03-14 15:25
**/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRankListDto {
    public UserRankListDto(Page<UserProfile> p)
    {
        results = new LinkedList<>();
        for(UserProfile tmp : p)
        {
            results.add(new UserRankDto(tmp));
        }
        total = p.getTotalElements();
    }
    private List<UserRankDto> results;
    private Long total;
}
