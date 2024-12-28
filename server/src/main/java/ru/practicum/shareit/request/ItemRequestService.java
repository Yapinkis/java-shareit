package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.utility.PageUtility;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь c " + userId + " не обнаружен"));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setUser(user);
        itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    public List<ItemRequestDto> get(Long userId) {
        List<ItemRequest> itemRequests = itemRequestRepository.findRequestsByUser(userId);
        if (itemRequests.isEmpty()) {
            return Collections.emptyList();
        }
        List<ItemRequestDto> itemRequestsDto = itemRequests.stream().map(ItemRequestMapper::toItemRequestDto).toList();
        List<Long> requestIdList = itemRequestsDto.stream().map(ItemRequestDto::getId).toList();
        List<Item> items = itemRepository.findAllByRequestIdIn(requestIdList);
        for (ItemRequestDto itemRequestDto : itemRequestsDto) {
            List<Item> requestItems = items.stream().filter(item ->
                    item.getRequest().getId().equals(itemRequestDto.getId())).toList();
            if (!requestItems.isEmpty()) {
                List<ItemDto> itemsDto = requestItems.stream().map(ItemMapper::toItemDto).toList();
                itemRequestDto.setItems(itemsDto);
            }
        }
        return itemRequestsDto;
    }

    public List<ItemRequestDto> getAll(Long userId, Long from, Long size) {
        PageRequest pageRequest = PageUtility.form(from.intValue(), size.intValue(), Sort.Direction.ASC,"created");
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByUserIdNot(userId,pageRequest);
        List<ItemRequestDto> itemRequestsDto = itemRequests.stream().map(ItemRequestMapper::toItemRequestDto).toList();
        List<Long> requestIdList = itemRequestsDto.stream().map(ItemRequestDto::getId).toList();
        List<Item> items = itemRepository.findAllByRequestIdIn(requestIdList);
        for (ItemRequestDto itemRequestDto : itemRequestsDto) {
            List<Item> requestItems = items.stream().filter(item ->
                    item.getRequest().getId().equals(itemRequestDto.getId())).toList();
            if (!requestItems.isEmpty()) {
                List<ItemDto> itemsDto = requestItems.stream().map(ItemMapper::toItemDto).toList();
                itemRequestDto.setItems(itemsDto);
            }
        }
        return itemRequestsDto;
    }

    public ItemRequestDto getById(Long userId, Long requestId) {
        ItemRequest itemRequest = itemRequestRepository.findItemRequestById(requestId).orElseThrow(()
                -> new EntityNotFoundException("Request не обнаружен в БД"));
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        List<Item> items = itemRepository.findAllByRequest_Id(itemRequestDto.getId());
        if (!items.isEmpty()) {
            List<ItemDto> itemsDto = items.stream().map(ItemMapper::toItemDto).toList();
            itemRequestDto.setItems(itemsDto);
        }
        return itemRequestDto;
    }
}
