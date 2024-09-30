package com.sb.studyBoard_Backend.service;


import java.util.List;
import org.springframework.stereotype.Service;

import com.sb.studyBoard_Backend.model.Group;
import com.sb.studyBoard_Backend.repository.GroupRepository;

@Service
public class GroupService {
    private GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

      public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    
}
