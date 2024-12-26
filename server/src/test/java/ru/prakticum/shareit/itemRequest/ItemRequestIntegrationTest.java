package ru.prakticum.shareit.itemRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestDto;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ShareItServer.class)
@Transactional
public class ItemRequestIntegrationTest {
    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    private User requester;

    @BeforeEach
    void setUp() {
        requester = new User();
        requester.setName("Test User");
        requester.setEmail("testuser@example.com");
        requester = userRepository.save(requester);
    }

    @Test
    void createItemRequest_ShouldSaveRequestToDatabase() {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Test request description");

        ItemRequestDto createdRequest = itemRequestService.createItemRequest(requestDto, requester.getId());

        assertNotNull(createdRequest.getId());
        assertEquals("Test request description", createdRequest.getDescription());

        Optional<ItemRequest> savedRequest = itemRequestRepository.findById(createdRequest.getId());
        assertTrue(savedRequest.isPresent());
        assertEquals(requester.getId(), savedRequest.get().getUser().getId());
    }

    @Test
    void getRequestsByUser_ShouldReturnRequests() {
        ItemRequest request = new ItemRequest();
        request.setDescription("User's request");
        request.setUser(requester);
        request.setCreated(LocalDateTime.now());
        itemRequestRepository.save(request);

        List<ItemRequestDto> userRequests = itemRequestService.get(requester.getId());

        assertFalse(userRequests.isEmpty());
        assertEquals(1, userRequests.size());
        assertEquals("User's request", userRequests.get(0).getDescription());
    }

    @Test
    void getAllRequests_ShouldReturnRequestsExcludingUser() {
        User anotherUser = new User();
        anotherUser.setName("Another User");
        anotherUser.setEmail("anotheruser@example.com");
        anotherUser = userRepository.save(anotherUser);

        ItemRequest request = new ItemRequest();
        request.setDescription("Another user's request");
        request.setUser(anotherUser);
        request.setCreated(LocalDateTime.now());
        itemRequestRepository.save(request);

        List<ItemRequestDto> allRequests = itemRequestService.getAll(requester.getId(), 0L, 10L);

        assertFalse(allRequests.isEmpty());
        assertEquals(1, allRequests.size());
        assertEquals("Another user's request", allRequests.get(0).getDescription());
    }

    @Test
    void getRequestById_ShouldReturnCorrectRequest() {
        ItemRequest request = new ItemRequest();
        request.setDescription("Specific request");
        request.setUser(requester);
        request.setCreated(LocalDateTime.now());
        request = itemRequestRepository.save(request);

        ItemRequestDto requestDto = itemRequestService.getById(requester.getId(), request.getId());
        assertNotNull(requestDto);
        assertEquals(request.getId(), requestDto.getId());
        assertEquals("Specific request", requestDto.getDescription());
    }
}
