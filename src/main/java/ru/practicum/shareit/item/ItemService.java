package ru.practicum.shareit.item;


import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

interface ItemService {
    ItemDto create(ItemDto item, Long userId);

    ItemDto update(ItemDto item, Long userId);

    ItemDto get(Long id);

    List<ItemDto> searchItem(String search);

    List<ItemDto> getAllFromUser(Long id);

}
