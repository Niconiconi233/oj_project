package com.yw.ojproject.dao;

import com.yw.ojproject.entity.ProblemTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProblemTagDao extends JpaRepository<ProblemTag, String>, JpaSpecificationExecutor<ProblemTag> {

}
