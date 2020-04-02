package com.yw.ojproject.dto;

import com.yw.ojproject.entity.Submission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-04-02 21:17
**/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionListDto {
    public SubmissionListDto(Page<Submission> p)
    {
        results = new ArrayList<>(p.getContent().size());
        for(int i = 0; i < p.getContent().size(); ++i)
        {
            results.add(i, new SubmissionSimpleDto(p.getContent().get(i)));
        }
        this.total = p.getTotalElements();
    }
    public SubmissionListDto(List<Submission> list, Integer offset, Integer size)
    {
        results = new ArrayList<>(size);
        for(int i = 0; i < size; ++i)
        {
            if((offset + i) >= list.size())
            {
                break;
            }
            results.add(i, new SubmissionSimpleDto(list.get(offset + i)));
        }
        this.total = Integer.valueOf(list.size()).longValue();
    }
    private List<SubmissionSimpleDto> results;
    private Long total;
}
