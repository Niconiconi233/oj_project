package com.yw.ojproject.dto;

import com.yw.ojproject.entity.Announcement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.LinkedList;
import java.util.List;

/**
* @program: ojproject
*
* @description: 公告列表DTO
*
* @author: YW
*
* @create: 2020-03-14 14:41
**/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementListDto {
    public AnnouncementListDto(Page<Announcement> page) {
        this.results = new LinkedList<>();
        for (Announcement a : page.getContent())
        {
            this.results.add(new AnnouncementDto(a));
        }
        this.total = page.getTotalElements();
    }
    private List<AnnouncementDto> results;
    private Long total;
}
