package ru.prakticum.shareit.itemRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.ItemRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTest {
    @InjectMocks
    private ItemRequestService itemRequestService;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    private User user;
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");

        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Test Request");
        itemRequest.setUser(user);
        itemRequest.setCreated(LocalDateTime.now());

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("Test Request");
        itemRequestDto.setCreated(LocalDateTime.now());
    }

    @Test
    void createItemRequest_ShouldReturnCreatedRequest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestDto result = itemRequestService.createItemRequest(itemRequestDto, 1L);

        assertNotNull(result);
        assertEquals("Test Request", result.getDescription());
        verify(itemRequestRepository, times(1)).save(any(ItemRequest.class));
    }

    @Test
    void get_ShouldReturnUserRequests() {
        when(itemRequestRepository.findRequestsByUser(1L)).thenReturn(List.of(itemRequest));
        when(itemRepository.findAllByRequestIdIn(List.of(1L))).thenReturn(List.of());

        List<ItemRequestDto> result = itemRequestService.get(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Request", result.get(0).getDescription());
        verify(itemRequestRepository, times(1)).findRequestsByUser(1L);
    }

    @Test
    void getAll_ShouldReturnAllRequestsExcludingUser() {
        when(itemRequestRepository.findAllByUserIdNot(eq(1L), any(PageRequest.class))).thenReturn(List.of(itemRequest));
        when(itemRepository.findAllByRequestIdIn(List.of(1L))).thenReturn(List.of());

        List<ItemRequestDto> result = itemRequestService.getAll(1L, 0L, 10L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Request", result.get(0).getDescription());
        verify(itemRequestRepository, times(1)).findAllByUserIdNot(eq(1L), any(PageRequest.class));
    }

    @Test
    void getById_ShouldReturnRequestWithItems() {
        when(itemRequestRepository.findItemRequestById(1L)).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByRequest_Id(1L)).thenReturn(List.of());

        ItemRequestDto result = itemRequestService.getById(1L, 1L);

        assertNotNull(result);
        assertEquals("Test Request", result.getDescription());
        verify(itemRequestRepository, times(1)).findItemRequestById(1L);
    }
}
