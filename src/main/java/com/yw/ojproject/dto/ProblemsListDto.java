package com.yw.ojproject.dto;

import com.yw.ojproject.entity.Problem;
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
* @create: 2020-03-18 20:24
**/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemsListDto {
    public ProblemsListDto(Page<Problem> p)
    {
        result = new LinkedList<>();
        for(Problem tmp : p.getContent())
        {
            result.add(new ProblemsDto(tmp));
        }
        total = p.getTotalElements();
    }
    private List<ProblemsDto> result;
    private Long total;
}
