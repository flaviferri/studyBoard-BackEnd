package com.sb.studyBoard_Backend.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sb.studyBoard_Backend.dto.*;
import com.sb.studyBoard_Backend.model.*;
import org.springframework.stereotype.Service;

import com.sb.studyBoard_Backend.repository.GroupRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class GroupService implements IGroupService {
    private GroupRepository groupRepository;
    private AuthService authService;
    private UserService userService;
    private RoleService roleService;
    private UserGroupRoleService userGroupRoleService;

   @Transactional 
    public GroupDTO createGroup(Group group) {
       String username = authService.getAuthenticatedUsername();
       UserEntity user = userService.findByUsername(username)
               .orElseThrow(() -> new RuntimeException("User not found"));

       group.setCreatedBy(user);

       if (group.getBoards() != null) {
           for (Board board : group.getBoards()) {
               board.setGroup(group);
               board.setCreatedBy(user);
           }
       }
       Group createdGroup = groupRepository.save(group);/**/
       RoleEntity createdRole = roleService.findByRoleEnum(RoleEnum.CREATED)
               .orElseThrow(() -> new RuntimeException("CREATED role not found"));

       boolean hasRoleCreated = user.getRoles().stream()
               .anyMatch(role -> role.getRoleEnum() == RoleEnum.CREATED);

       if (!hasRoleCreated) {
           user.getRoles().add(createdRole);
           userGroupRoleService.saveUser(user);
       } else {
           System.out.println("El usuario ya tiene el rol CREATED.");
       }

       UserGroupRole userGroupRole = new UserGroupRole();
       userGroupRole.setUser(user);
       userGroupRole.setGroup(createdGroup);
       userGroupRole.setRole(createdRole);
       userGroupRoleService.save(userGroupRole);
       return convertToDTO(createdGroup, user);
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
                    boardDTO.setId(board.getId());
                    boardDTO.setTitle(board.getTitle());
                    boardDTO.setColor(board.getColor());
                    return boardDTO;
                })
                .collect(Collectors.toSet()));

        dto.setIsCreator(Objects.equals(user.getId(), group.getCreatedBy().getId()));

        return dto;
    }
}

