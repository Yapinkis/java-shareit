package ru.practicum.shareit.item;

import lombok.Data;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
@Data
public class ItemRepositoryImpl implements ItemRepository {
    private Long id = 0L;
    private Map<Long, Item> items = new HashMap<>();

    @Override
    public ItemDto create(Item item) {
        item.setId(id);
        items.put(id++,item);
        return ItemMapper.toItemDTO(item);
    }

    @Override
    public ItemDto update(Item item) {
        Item updatedItem = items.get(item.getId());
        Optional.ofNullable(item.getName()).ifPresent(updatedItem::setName);
        Optional.ofNullable(item.getDescription()).ifPresent(updatedItem::setDescription);
        Optional.ofNullable(item.getAvailable()).ifPresent(updatedItem::setAvailable);
        Optional.ofNullable(item.getOwner()).ifPresent(updatedItem::setOwner);
        Optional.ofNullable(item.getRequest()).ifPresent(updatedItem::setRequest);
        items.put(item.getId(),updatedItem);
        return ItemMapper.toItemDTO(updatedItem);
    }

    @Override
    public ItemDto get(Long id) {
        Item item = items.get(id);
        return ItemMapper.toItemDTO(item);
    }

    @Override
    public List<ItemDto> searchItem(String search) {
        List<ItemDto> itemList = new ArrayList<>();
        String searchTrimmed = search.trim().toLowerCase();
        for (Item item : items.values()) {
            if (item.getName().trim().toLowerCase().equals(searchTrimmed) && item.getAvailable()) {
                itemList.add(ItemMapper.toItemDTO(item));
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
                itemsDTO.add(ItemMapper.toItemDTO(item));
            }
        }
        return itemsDTO;
    }


}
