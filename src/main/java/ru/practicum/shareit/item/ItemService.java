package ru.practicum.shareit.item;


import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

interface ItemService {
    ItemDto create(Item item, Long userId);

    ItemDto update(Item item, Long userId);

    ItemDto get(Long id);

    List<ItemDto> searchItem(String search);

    List<ItemDto> getAllFromUser(Long id);

}
