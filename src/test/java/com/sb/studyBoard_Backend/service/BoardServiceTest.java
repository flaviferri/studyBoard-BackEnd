package com.sb.studyBoard_Backend.service;

import com.sb.studyBoard_Backend.exceptions.GroupHasNoBoards;
import com.sb.studyBoard_Backend.exceptions.GroupNotFoundException;
import com.sb.studyBoard_Backend.interfaces.IBoardService;
import com.sb.studyBoard_Backend.model.Board;
import com.sb.studyBoard_Backend.model.Group;
import com.sb.studyBoard_Backend.model.Postit;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.repository.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
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

    @Mock
    private PostItService postItService;

    @InjectMocks
    private BoardService boardService;
    private IBoardService iBoardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        iBoardService = boardService;
    }

    @Test
    public void addBoard_GroupDoesNotExist_ThrowsException() {
        Long groupId = 1L;
        Board board = new Board();
        String username = "testUser";
        UserEntity user = new UserEntity();

        when(authService.getAuthenticatedUsername()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(groupService.getGroupById(groupId)).thenReturn(Optional.empty());

        assertThrows(GroupNotFoundException.class, () -> iBoardService.addBoard(board, groupId));
    }

    @Test
    public void addBoard_UserDoesNotExist_ThrowsException() {
        Long groupId = 1L;
        Board board = new Board();
        String username = "testUser";

        when(authService.getAuthenticatedUsername()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> iBoardService.addBoard(board, groupId));
    }

    @Test
    public void addBoard_UserIsNotCreator_ThrowsException() {
        Long groupId = 1L;
        Board board = new Board();
        String username = "testUser";
        Group group = new Group();
        group.setId(groupId);
        UserEntity user = new UserEntity();
        user.setId(1L);
        UserEntity creator = new UserEntity();
        creator.setId(2L);
        group.setCreatedBy(creator);

        when(authService.getAuthenticatedUsername()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(groupService.getGroupById(groupId)).thenReturn(Optional.of(group));

        assertThrows(AccessDeniedException.class, () -> iBoardService.addBoard(board, groupId));
    }

    @Test
    public void addBoard_BoardCreated_ReturnsBoard() {
        Long groupId = 1L;
        Board board = new Board();
        String username = "testUser";
        Group group = new Group();
        group.setId(groupId);
        UserEntity user = new UserEntity();
        user.setId(1L);
        group.setCreatedBy(user);
        Postit defaultPostit = new Postit();

        when(authService.getAuthenticatedUsername()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(groupService.getGroupById(groupId)).thenReturn(Optional.of(group));
        when(postItService.createInstructionsPostIt(any(Board.class))).thenReturn(defaultPostit);

        ResponseEntity<Object> response = boardService.addBoard(board, groupId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(board, response.getBody());
        verify(boardRepository).save(board);
    }

    @Test
    public void getAllBoards_GroupExists_ReturnsAllBoards() {
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

        when(groupService.getGroupById(groupId)).thenReturn(Optional.of(group));

        Set<Board> result = iBoardService.getAllBoards(groupId);

        assertEquals(1, result.size());
        assertEquals(board, result.iterator().next());
    }

    @Test
    public void getAllBoards_GroupDoesNotExist_ThrowsException() {
        Long groupId = 1L;

        when(groupService.getGroupById(groupId)).thenReturn(Optional.empty());

        assertThrows(GroupNotFoundException.class, () -> iBoardService.getAllBoards(groupId));
    }

    @Test
    public void getAllBoards_GroupHasNoBoards_ThrowsException() {
        Long groupId = 1L;
        Group group = new Group();
        group.setId(groupId);
        group.setBoards(Set.of());

        when(groupService.getGroupById(groupId)).thenReturn(Optional.of(group));

        assertThrows(GroupHasNoBoards.class, () -> iBoardService.getAllBoards(groupId));
    }
}
