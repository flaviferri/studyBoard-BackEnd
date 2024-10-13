package com.sb.studyBoard_Backend.service;

import com.sb.studyBoard_Backend.exceptions.GroupHasNoBoards;
import com.sb.studyBoard_Backend.exceptions.GroupNotFoundException;
import com.sb.studyBoard_Backend.interfaces.IBoardService;
import com.sb.studyBoard_Backend.model.Board;
import com.sb.studyBoard_Backend.model.Group;
import com.sb.studyBoard_Backend.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class BoardServiceTest {

    @Mock
    private GroupService groupService;

    @InjectMocks
    private BoardService boardService;
    private IBoardService iBoardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        iBoardService = boardService;
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
