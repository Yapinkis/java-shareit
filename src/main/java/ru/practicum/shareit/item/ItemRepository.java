package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

interface ItemRepository {
    ItemDto create(ItemDto item);

    ItemDto update(ItemDto item);

    ItemDto get(Long id);

    List<ItemDto> searchItem(String search);

    List<ItemDto> getAllFromUser(List<Long> keys);
}
