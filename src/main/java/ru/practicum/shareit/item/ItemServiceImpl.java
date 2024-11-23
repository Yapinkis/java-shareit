package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.utility.Utility;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final Utility utilityValidator;

    @Override
    public ItemDto create(ItemDto item, Long userId) {
        item.setOwner(userId);
        utilityValidator.checkUserId(userId);
        return itemRepository.create(item);
    }

    @Override
    public ItemDto update(ItemDto item, Long userId) {
        utilityValidator.checkUserId(userId);
        item.setId(utilityValidator.getItemFromUser(userId));
        return itemRepository.update(item);
    }

    @Override
    public ItemDto get(Long id) {
        return itemRepository.get(id);
    }

    @Override
    public List<ItemDto> searchItem(String search) {
        return itemRepository.searchItem(search);
    }

    @Override
    public List<ItemDto> getAllFromUser(Long id) {
        return itemRepository.getAllFromUser(utilityValidator.getKeys(id));
    }

}
