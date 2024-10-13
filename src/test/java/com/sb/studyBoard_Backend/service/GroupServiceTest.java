package com.sb.studyBoard_Backend.service;

import com.sb.studyBoard_Backend.dto.GroupDTO;
import com.sb.studyBoard_Backend.interfaces.IGroupService;
import com.sb.studyBoard_Backend.model.Group;
import com.sb.studyBoard_Backend.model.RoleEntity;
import com.sb.studyBoard_Backend.model.RoleEnum;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GroupServiceTest {
    @Mock
    private GroupRepository groupRepository;

    @Mock
    private BoardService boardService;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @Mock
    private PostItService postItService;

    @Mock
    private RoleService roleService;

    @Mock
    private UserGroupRoleService userGroupRoleService;

    @InjectMocks
    private GroupService groupService;
    private IGroupService iGroupService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        iGroupService = groupService;
    }

    @Test
    public void createGroup_CreatesGroup(){
        String username = "testUser";
        UserEntity user = new UserEntity();
        user.setRoles(new ArrayList<>());
        user.setId(1L);
        Group group = new Group();
        group.setGroupName("Test Group");

        when(authService.getAuthenticatedUsername()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(groupRepository.save(any(Group.class))).thenReturn(group);
        when(roleService.findByRoleEnum(RoleEnum.CREATED)).thenReturn(Optional.of(new RoleEntity()));

        GroupDTO createdGroupDTO = iGroupService.createGroup(group);

        assertNotNull(createdGroupDTO);
        assertEquals("Test Group", createdGroupDTO.getGroupName());
        verify(groupRepository).save(group);
        verify(userGroupRoleService).saveUser(user);
    }

    @Test
    public void createGroup_UserDoesNotExist_ThrowsException(){
        String username = "testUser";
        Group group = new Group();
        group.setGroupName("Test Group");

        when(authService.getAuthenticatedUsername()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> iGroupService.createGroup(group));
    }

    @Test
    public void createGroup_CreatedRoleNotFound_ThrowsException(){
        String username = "testUser";
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setRoles(new ArrayList<>());
        Group group = new Group();
        group.setGroupName("Test Group");

        when(authService.getAuthenticatedUsername()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(roleService.findByRoleEnum(RoleEnum.CREATED)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> iGroupService.createGroup(group));
    }

    @Test
    public void getAllGroups_ReturnsAllGroups(){
        String username = "testUser";
        UserEntity user = new UserEntity();
        user.setId(1L);
        Group group1 = new Group();
        group1.setId(1L);
        group1.setCreatedBy(user);
        Group group2 = new Group();
        group2.setId(2L);
        group2.setCreatedBy(user);
        List<Group> groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);

        when(authService.getAuthenticatedUsername()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(groupRepository.findAll()).thenReturn(groups);

        List<GroupDTO> result = groupService.getAllGroups();

        assertEquals(2, result.size());
        assertEquals(group1.getId(), result.get(0).getId());
        assertEquals(group2.getId(), result.get(1).getId());
    }

    @Test
    public void getAllGroups_UserDoesNotExist_ThrowsException(){
        String username = "testUser";
        Group group = new Group();
        group.setGroupName("Test Group");

        when(authService.getAuthenticatedUsername()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> iGroupService.createGroup(group));
    }
}
