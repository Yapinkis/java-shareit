package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestBody @Valid Item item, @RequestHeader("X-Sharer-User-Id") Long id) {
        log.info("Создан предмет ={} принадлежащий пользователю ={}", item.getName(), id);
        return itemService.create(item, id);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestBody Item item, @RequestHeader("X-Sharer-User-Id") Long id) {
        return itemService.update(item,id);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllFromUser(userId);
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable Long id) {
        return itemService.get(id);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam("text") String search) {
        return itemService.searchItem(search);
    }

}
