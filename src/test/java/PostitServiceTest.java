import com.sb.studyBoard_Backend.model.*;
import com.sb.studyBoard_Backend.repository.BoardRepository;
import com.sb.studyBoard_Backend.repository.PostitRepository;
import com.sb.studyBoard_Backend.repository.UserRepository;
import com.sb.studyBoard_Backend.service.PostitService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostitServiceTest {

    @InjectMocks
    private PostitService postitService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private PostitRepository postitRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePostit_Success() throws Exception {
        UserEntity mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setName("Test User");

        RoleEntity mockRole = new RoleEntity();
        mockRole.setRoleEnum(RoleEnum.USER);
        PermissionEntity mockPermission = new PermissionEntity();
        mockPermission.setName("CREATE_POSTIT");
        mockRole.setPermissionsEntity(new HashSet<>(Collections.singletonList(mockPermission)));

        mockUser.setRoles(new HashSet<>(Collections.singletonList(mockRole)));

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        Board mockBoard = new Board();
        mockBoard.setId(1L);
        when(boardRepository.findById(1L)).thenReturn(Optional.of(mockBoard));

        Postit postit = new Postit();

        when(postitRepository.save(any(Postit.class))).thenReturn(postit);

        Postit createdPostit = postitService.createPostit(postit, 1L, 1L);

        assertNotNull(createdPostit);
        assertEquals(mockUser, createdPostit.getCreatedBy());
        assertEquals(mockBoard, createdPostit.getBoard());
    }
}

   /* @Test
    public void testCreatePostit_AccessDenied() {
        UserEntity mockUser = new UserEntity();
        mockUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        Board mockBoard = new Board();
        mockBoard.setId(1L);
        when(boardRepository.findById(1L)).thenReturn(Optional.of(mockBoard));

        // Simulate no permission
        when(postitService.hasPermission(mockUser, "CREATE_POSTIT")).thenReturn(false);

        Postit postit = new Postit();

        assertThrows(AccessDeniedException.class, () -> {
            postitService.createPostit(postit, 1L, 1L);
        });
    }

    @Test
    public void testGetPostitById_Success() throws Exception {
        UserEntity mockUser = new UserEntity();
        mockUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // Simulate permission check
        when(postitService.hasPermission(mockUser, "READ_POSTIT")).thenReturn(true);

        Postit mockPostit = new Postit();
        mockPostit.setId(1L);

        when(postitRepository.findById(1L)).thenReturn(Optional.of(mockPostit));

        Postit retrievedPostit = postitService.getPostitById(1L, 1L);

        assertNotNull(retrievedPostit);
        assertEquals(1L, retrievedPostit.getId());
    }

    @Test
    public void testDeletePostit_Success() throws Exception {
        UserEntity mockUser = new UserEntity();
        mockUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // Simulate permission check
        when(postitService.hasPermission(mockUser, "DELETE_POSTIT")).thenReturn(true);

        Postit mockPostit = new Postit();
        mockPostit.setId(1L);
        when(postitRepository.findById(1L)).thenReturn(Optional.of(mockPostit));

        postitService.deletePostit(1L, 1L);

        verify(postitRepository, times(1)).delete(mockPostit);
    }*/
