package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ru.practicum.shareit.utility.Utility;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(@RequestBody @Valid ItemRequestDto itemRequestDto,
                                      @RequestHeader(Utility.X_SHARER_USER_ID) Long userId) {
        return itemRequestService.createItemRequest(itemRequestDto,userId);
    }

    @GetMapping
    public List<ItemRequestDto> get(@RequestHeader(Utility.X_SHARER_USER_ID) Long userId) {
        return itemRequestService.get(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestHeader(Utility.X_SHARER_USER_ID) Long userId,
                                       @RequestParam(defaultValue = "0") Long from,
                                       @RequestParam(defaultValue = "10") Long size)
        throws HandlerMethodValidationException {
            return itemRequestService.getAll(userId,from,size);
    }

    @GetMapping("{requestId}")
    public ItemRequestDto getById(@RequestHeader(Utility.X_SHARER_USER_ID) Long userId,
                              @PathVariable Long requestId) {
        return itemRequestService.getById(userId, requestId);
    }

}
