package com.sb.studyBoard_Backend.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sb.studyBoard_Backend.dto.*;
import com.sb.studyBoard_Backend.model.UserEntity;
import org.springframework.stereotype.Service;

import com.sb.studyBoard_Backend.model.Group;
import com.sb.studyBoard_Backend.repository.GroupRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class GroupService {
    private GroupRepository groupRepository;
    private AuthService authService;
    private UserService userService;

   @Transactional 
    public Group createGroup(Group group) {

        return groupRepository.save(group);
    }

    public List<GroupDTO> getAllGroups() {
        String username = authService.getAuthenticatedUsername();
        UserEntity user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Group> groups = groupRepository.findAll();
        return groups.stream()
                .map(group -> convertToDTO(group, user))
                .collect(Collectors.toList());
    }

    public Optional<Group> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

    public Optional<GroupDTO> findGroupDTOById(Long id) {
        String username = authService.getAuthenticatedUsername();
        UserEntity user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return Optional.ofNullable(convertToDTO(group, user));
    }

    public GroupDTO convertToDTO(Group group, UserEntity user) {
        GroupDTO dto = new GroupDTO();
        dto.setId(group.getId());
        dto.setGroupName(group.getGroupName());

        CreatedByDTO createdByDTO = new CreatedByDTO();
        createdByDTO.setId(group.getCreatedBy().getId());
        dto.setCreatedBy(createdByDTO);

        dto.setBoards(group.getBoards().stream()
                .map(board -> {
                    BoardDTO boardDTO = new BoardDTO();
                    boardDTO.setTitle(board.getTitle());
                    boardDTO.setColor(board.getColor());
                    return boardDTO;
                })
                .collect(Collectors.toSet()));

        dto.setIsCreator(Objects.equals(user.getId(), group.getCreatedBy().getId()));

        return dto;
    }

}