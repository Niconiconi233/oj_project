package com.yw.ojproject.dao;

import com.yw.ojproject.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AnnouncementDao extends JpaSpecificationExecutor<Announcement>, JpaRepository<Announcement, String> {
    /**
    * @Description: 查找可见公告
    * @Param: []
    * @return: java.util.List<com.yw.ojproject.entity.Announcement>
    * @Author: YW
    * @Date:
    */
    List<Announcement> findAllByVisibleTrue();
}
