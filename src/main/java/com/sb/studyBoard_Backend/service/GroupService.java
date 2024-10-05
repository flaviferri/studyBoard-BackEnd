package com.sb.studyBoard_Backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sb.studyBoard_Backend.model.Group;
import com.sb.studyBoard_Backend.repository.GroupRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class GroupService {
    private GroupRepository groupRepository;

   @Transactional 
    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<Group> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

}