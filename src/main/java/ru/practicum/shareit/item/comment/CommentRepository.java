package ru.practicum.shareit.item.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.item.id = ?1")
    List<Comment> getComments(Long itemId);

    @Query("SELECT c FROM Comment c WHERE c.item.id IN :itemsIds")
    List<Comment> getAllComments(List<Long> itemsIds);
}
