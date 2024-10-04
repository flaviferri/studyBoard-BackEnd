package com.sb.studyBoard_Backend.service;

import org.springframework.stereotype.Service;

import com.sb.studyBoard_Backend.model.Group;
import com.sb.studyBoard_Backend.repository.GroupRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class GroupService {
    private GroupRepository groupRepository;

    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

}