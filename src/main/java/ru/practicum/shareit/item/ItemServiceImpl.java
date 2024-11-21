package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.utility.UtilityValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UtilityValidator utilityValidator;

    @Override
    public ItemDto create(Item item, Long userId) {
        item.setOwner(userId);
        utilityValidator.checkUserId(userId);
        return itemRepository.create(item);
    }

    @Override
    public ItemDto update(Item item, Long userId) {
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
