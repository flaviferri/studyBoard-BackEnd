package com.sb.studyBoard_Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sb.studyBoard_Backend.model.Group;
import org.springframework.data.repository.query.Param;


import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findByCreatedBy_Id(Long userId);

}

