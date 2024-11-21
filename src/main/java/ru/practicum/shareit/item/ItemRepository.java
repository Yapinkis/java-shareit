package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

interface ItemRepository {
    ItemDto create(Item item);

    ItemDto update(Item item);

    ItemDto get(Long id);

    List<ItemDto> searchItem(String search);

    List<ItemDto> getAllFromUser(List<Long> keys);
}
