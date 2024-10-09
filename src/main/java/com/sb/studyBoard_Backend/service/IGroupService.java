package com.sb.studyBoard_Backend.service;


import com.sb.studyBoard_Backend.dto.GroupDTO;
import com.sb.studyBoard_Backend.model.Group;
import com.sb.studyBoard_Backend.model.UserEntity;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IGroupService {
        GroupDTO createGroup(Group group);
        GroupDTO joinGroup(Long groupId);
        List<Group> findByUserId(Long userId);
        List<Group> findByCreatedBy_Id(Long userId);
        List<GroupDTO> getAllGroups();
        List<GroupDTO> getUserGroups();
        Optional<Group> getGroupById(Long id);
        Optional<GroupDTO> findGroupDTOById(Long id);
        GroupDTO convertToDTO(Group group, UserEntity user);
}