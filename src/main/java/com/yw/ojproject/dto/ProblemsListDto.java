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
* @description: 问题列表DTO
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
        results = new LinkedList<>();
        for(Problem tmp : p.getContent())
        {
            results.add(new ProblemsDto(tmp));
        }
        total = p.getTotalElements();
    }
    public ProblemsListDto(List<Problem> p, Integer size, Integer offset)
    {
        results = new LinkedList<>();
        while(offset < p.size())
        {
            results.add(new ProblemsDto(p.get(offset)));
            ++offset;
        }
        Integer s = p.size();
        total = s.longValue();
    }
    private List<ProblemsDto> results;
    private Long total;
}
