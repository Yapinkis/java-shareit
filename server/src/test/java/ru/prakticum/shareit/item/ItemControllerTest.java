package ru.prakticum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.utility.Utility;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = ItemController.class)
@ContextConfiguration(classes = ShareItServer.class)
public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    private ItemDto itemDto;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Test Item");
        itemDto.setDescription("Description");
        itemDto.setAvailable(true);

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Great item!");
    }

    @Test
    void create_ShouldReturnCreatedItem() throws Exception {
        when(itemService.create(any(ItemDto.class), eq(1L))).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header(Utility.X_SHARER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Item"));

        verify(itemService, times(1)).create(any(ItemDto.class), eq(1L));
    }

    @Test
    void update_ShouldReturnNotFound_WhenItemDoesNotExist() throws Exception {
        when(itemService.update(any(ItemDto.class), eq(1L)))
                .thenThrow(new EntityNotFoundException("Item not found"));

        mockMvc.perform(patch("/items/{id}", 1L)
                        .header(Utility.X_SHARER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Item not found"));
    }

    @Test
    void get_ShouldReturnItem() throws Exception {
        when(itemService.get(1L, 1L)).thenReturn(itemDto);

        mockMvc.perform(get("/items/{id}", 1L)
                        .header(Utility.X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Item"));

        verify(itemService, times(1)).get(1L, 1L);
    }

    @Test
    void addComment_ShouldReturnComment() throws Exception {
        when(itemService.addComment(eq(1L), eq(1L), any(CommentDto.class))).thenReturn(commentDto);

        mockMvc.perform(post("/items/{id}/comment", 1L)
                        .header(Utility.X_SHARER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Great item!"));

        verify(itemService, times(1)).addComment(eq(1L), eq(1L), any(CommentDto.class));
    }

    @Test
    void getAll_ShouldHandleMissingHeaderProperly() throws Exception {
        mockMvc.perform(get("/items"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Required request header 'X-Sharer-User-Id' for method parameter type Long is not present"));
    }

    @Test
    void searchItem_ShouldReturnEmptyList_WhenNoMatchesFound() throws Exception {
        when(itemService.searchItem("nonexistent")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items/search")
                        .param("text", "nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }




}
