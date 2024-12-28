package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.utility.Utility;

@Controller
@RequestMapping(path = "/items")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ItemRequestDto itemRequestDto,
                                 @RequestHeader(Utility.X_SHARER_USER_ID) Long id) {
        log.info("Для Пользователя с Id ={}", id);
        return itemClient.create(id, itemRequestDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody @Valid ItemRequestDto itemRequestDto,
                          @RequestHeader(Utility.X_SHARER_USER_ID) Long id,
                          @PathVariable Long itemId) {
        log.info("Для Пользователя с Id ={}", id);
        return itemClient.update(id,itemId,itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(Utility.X_SHARER_USER_ID) Long userId,
                                         @RequestParam(defaultValue = "0") Long from,
                                         @RequestParam(defaultValue = "10") Long size) {
        return itemClient.getAllFromUser(userId,from,size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable Long id,
                                      @RequestHeader(Utility.X_SHARER_USER_ID) Long userId) {
        return itemClient.get(id,userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam(Utility.TEXT) String search,
                                    @RequestHeader(Utility.X_SHARER_USER_ID) Long userId,
                                    @RequestParam(defaultValue = "0") Long from,
                                    @RequestParam(defaultValue = "10") Long size) {
        return itemClient.searchItem(search,userId,from,size);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(Utility.X_SHARER_USER_ID) Long userId,
                                 @PathVariable Long id,
                                 @RequestBody CommentRequestDto commentRequestDto) {
        return itemClient.addComment(userId,id,commentRequestDto);
    }
}
