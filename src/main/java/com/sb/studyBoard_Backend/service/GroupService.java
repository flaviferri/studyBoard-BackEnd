package com.sb.studyBoard_Backend.service;

import com.sb.studyBoard_Backend.dto.BoardDTO;
import com.sb.studyBoard_Backend.dto.CreatedByDTO;
import com.sb.studyBoard_Backend.dto.GroupDTO;
import com.sb.studyBoard_Backend.interfaces.IGroupService;
import com.sb.studyBoard_Backend.model.*;
import com.sb.studyBoard_Backend.repository.GroupRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class GroupService implements IGroupService {
        private GroupRepository groupRepository;
        private AuthService authService;
        private UserService userService;
        private RoleService roleService;
        private PostItService postItService;
        private UserGroupRoleService userGroupRoleService;

        @Transactional
        public GroupDTO createGroup(Group group) {
                String username = authService.getAuthenticatedUsername();
                UserEntity user = userService.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                group.setCreatedBy(user);

                if (group.getBoards() != null) {
                        for (Board board : group.getBoards()) {
                                BoardService.addInstructionsPostIt(board,user,group, postItService);
                        }
                }

                Group createdGroup = groupRepository.save(group);

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

        @Transactional
        public GroupDTO joinGroup(Long groupId) {
                String username = authService.getAuthenticatedUsername();
                UserEntity user = userService.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                Group group = groupRepository.findById(groupId)
                                .orElseThrow(() -> new RuntimeException("Group not found"));
                boolean alreadyMember = user.getUserGroupRoles().stream()
                                .anyMatch(ugr -> ugr.getGroup().getId().equals(groupId));

                if (alreadyMember) {
                        throw new RuntimeException("User already a member of this group");
                }

                RoleEntity userRole = roleService.findByRoleEnum(RoleEnum.USER)
                                .orElseThrow(() -> new RuntimeException("User role not found"));

                UserGroupRole userGroupRole = new UserGroupRole();
                userGroupRole.setUser(user);
                userGroupRole.setGroup(group);
                userGroupRole.setRole(userRole);
                userGroupRoleService.save(userGroupRole);

                return convertToDTO(group, user);

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

        @Override
        public List<GroupDTO> getUserGroups() {
                String username = authService.getAuthenticatedUsername();
                UserEntity user = userService.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                List<Group> groups = user.getUserGroupRoles().stream()
                                .map(UserGroupRole::getGroup)
                                .collect(Collectors.toList());

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
                                        boardDTO.setId(board.getId());
                                        return boardDTO;
                                })
                                .collect(Collectors.toSet()));

                dto.setIsCreator(Objects.equals(user.getId(), group.getCreatedBy().getId()));

                dto.setIsMember(user.getUserGroupRoles().stream()
                                .anyMatch(ugr -> ugr.getGroup().getId().equals(group.getId())));

                return dto;
        }
}
