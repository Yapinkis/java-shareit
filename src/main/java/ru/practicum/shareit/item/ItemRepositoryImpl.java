package ru.practicum.shareit.item;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Slf4j
@Repository
@Data
public class ItemRepositoryImpl implements ItemRepository {
    private Long id = 0L;
    private Map<Long, Item> items = new HashMap<>();

    @Override
    public ItemDto create(ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        item.setId(id);
        items.put(id++,item);
        log.info("Создан предмет ={}", item.getName());
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(ItemDto itemDto) {
        Item updatedItem = items.get(itemDto.getId());
        Optional.ofNullable(itemDto.getName()).ifPresent(updatedItem::setName);
        Optional.ofNullable(itemDto.getDescription()).ifPresent(updatedItem::setDescription);
        Optional.ofNullable(itemDto.getAvailable()).ifPresent(updatedItem::setAvailable);
        Optional.ofNullable(itemDto.getOwner()).ifPresent(updatedItem::setOwner);
        Optional.ofNullable(itemDto.getRequest()).ifPresent(updatedItem::setRequest);
        items.put(itemDto.getId(),updatedItem);
        log.info("Обновлён предмет ={}", itemDto.getName());
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemDto get(Long id) {
        Item item = items.get(id);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> searchItem(String search) {
        List<ItemDto> itemList = new ArrayList<>();
        String searchTrimmed = search.trim().toLowerCase();
        for (Item item : items.values()) {
            if (item.getName().trim().toLowerCase().equals(searchTrimmed) && item.getAvailable()) {
                itemList.add(ItemMapper.toItemDto(item));
            }
        }
        return itemList;
    }

    @Override
    public List<ItemDto> getAllFromUser(List<Long> keys) {
        List<ItemDto> itemsDTO = new ArrayList<>();
        for (Long key : keys) {
            Item item = items.get(key);
            if (item != null) {
                itemsDTO.add(ItemMapper.toItemDto(item));
            }
        }
        return itemsDTO;
    }


}
