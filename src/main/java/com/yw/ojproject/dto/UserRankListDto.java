package com.yw.ojproject.dto;

import com.yw.ojproject.entity.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

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
        results = p.getContent();
        total = p.getTotalElements();
    }
    private List<UserProfile> results;
    private Long total;
}
