package com.sb.studyBoard_Backend.service;


import com.sb.studyBoard_Backend.dto.GroupDTO;
import com.sb.studyBoard_Backend.model.Group;
import com.sb.studyBoard_Backend.model.UserEntity;

import java.util.List;
import java.util.Optional;

public interface IGroupService {
        GroupDTO createGroup(Group group);

        List<GroupDTO> getAllGroups();

        Optional<Group> getGroupById(Long id);

        Optional<GroupDTO> findGroupDTOById(Long id);

        GroupDTO convertToDTO(Group group, UserEntity user);
    }

