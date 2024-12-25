package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.utility.Utility;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid RequestDto requestDto,
                                  @RequestHeader(Utility.X_SHARER_USER_ID)Long userId) {
        return itemRequestClient.create(requestDto,userId);
    }

    @GetMapping
    public ResponseEntity<Object> get(@RequestHeader(Utility.X_SHARER_USER_ID) Long userId) {
        return itemRequestClient.get(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader(Utility.X_SHARER_USER_ID) Long userId,
                                       @RequestParam(defaultValue = "0") Long from,
                                       @RequestParam(defaultValue = "10") Long size) {
        return itemRequestClient.getAll(userId,from,size);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader(Utility.X_SHARER_USER_ID) Long userId,
                                  @PathVariable Long requestId) {
        return itemRequestClient.getById(userId, requestId);
    }


}
