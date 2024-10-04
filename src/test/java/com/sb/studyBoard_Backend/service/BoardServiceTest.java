package com.sb.studyBoard_Backend.service;

import com.sb.studyBoard_Backend.exceptions.GroupHasNoBoards;
import com.sb.studyBoard_Backend.exceptions.GroupNotFoundException;
import com.sb.studyBoard_Backend.model.Board;
import com.sb.studyBoard_Backend.model.Group;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.repository.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class BoardServiceTest {
    @Mock
    private BoardRepository boardRepository;

    @Mock
    private GroupService groupService;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private BoardService boardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllBoards_UserExists_GroupExists_ReturnsAllBoards() {
        Long groupId = 1L;
        String username = "testUser";
        Group group = new Group();
        group.setId(groupId);
        UserEntity user = new UserEntity();
        user.setEmail(username);
        user.setId(1L);
        group.setCreatedBy(user);
        Board board = new Board();
        board.setCreatedBy(user);
        board.setGroup(group);
        group.setBoards(Set.of(board));

        when(authService.getAuthenticatedUsername()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(groupService.getGroupById(groupId)).thenReturn(Optional.of(group));

        Set<Board> result = boardService.getAllBoards(groupId);

        assertEquals(1, result.size());
        assertEquals(board, result.iterator().next());
    }

    @Test
    public void getAllBoards_GroupDoesNotExist_ThrowsException() {
        Long groupId = 1L;
        String username = "testUser";

        when(authService.getAuthenticatedUsername()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(new UserEntity()));
        when(groupService.getGroupById(groupId)).thenReturn(Optional.empty());

        assertThrows(GroupNotFoundException.class, () -> boardService.getAllBoards(groupId));
    }

    @Test
    public void getAllBoards_UserDoesNotExist_ThrowsException() {
        Long groupId = 1L;
        String username = "testUser";
        Group group = new Group();
        group.setId(groupId);
        group.setBoards(Set.of(new Board()));

        when(groupService.getGroupById(groupId)).thenReturn(Optional.of(group));
        when(authService.getAuthenticatedUsername()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> boardService.getAllBoards(groupId));
    }

    @Test
    public void getAllBoards_GroupHasNoBoards_ThrowsException() {
        Long groupId = 1L;
        String username = "testUser";
        Group group = new Group();
        group.setId(groupId);
        group.setBoards(Set.of());


        when(authService.getAuthenticatedUsername()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(new UserEntity()));
        when(groupService.getGroupById(groupId)).thenReturn(Optional.of(group));

        assertThrows(GroupHasNoBoards.class, () -> boardService.getAllBoards(groupId));
    }
}
