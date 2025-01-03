package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.utility.Utility;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestBody @Valid ItemDto item, @RequestHeader(Utility.X_SHARER_USER_ID) Long id) {
        log.info("Для Пользователя с Id ={}", id);
        return itemService.create(item, id);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestBody ItemDto item, @RequestHeader(Utility.X_SHARER_USER_ID) Long id) {
        log.info("Для Пользователя с Id ={}", id);
        return itemService.update(item,id);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(Utility.X_SHARER_USER_ID) Long userId) {
        return itemService.getAllFromUser(userId);
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable Long id, @RequestHeader(Utility.X_SHARER_USER_ID) Long userId) {
        return itemService.get(id, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam(Utility.TEXT) String search) {
        return itemService.searchItem(search);
    }

    @PostMapping("/{id}/comment")
    public CommentDto addComment(@RequestHeader(Utility.X_SHARER_USER_ID) Long userId, @PathVariable Long id, @RequestBody @Valid CommentDto comment) {
        return itemService.addComment(userId,id,comment);
    }

}
