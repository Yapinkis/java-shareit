package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.comment.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> create(Long id, ItemRequestDto itemRequestDto) {
        return post("", id,itemRequestDto);
    }

    public ResponseEntity<Object> update(Long id, Long itemId,ItemRequestDto itemRequestDto) {
        return patch("/" + itemId,id,itemRequestDto);
    }

    public ResponseEntity<Object> getAllFromUser(Long userId,Long from,Long size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}",userId,parameters);
    }

    public ResponseEntity<Object> get(Long userId, Long id) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> searchItem(String search,Long userId, Long from, Long size) {
        Map<String, Object> parameters = Map.of(
                "search",search,
                "from",from,
                "size",size
        );
        return get("/search?search={search}&from={from}&size={size}",userId,parameters);
    }

    public ResponseEntity<Object> addComment(Long userId, Long itemId, CommentRequestDto commentRequestDto) {
        return post("/" + itemId + "/comment", userId, commentRequestDto);
    }
}



