package ru.practicum.shareit.item.comment;

import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setItem(comment.getItem() != null ? ItemMapper.toItemDto(comment.getItem()) : null);
        commentDto.setUser(comment.getUser() != null ? UserMapper.toUserDto(comment.getUser()) : null);
        commentDto.setCreated(comment.getCommentTime());
        commentDto.setAuthorName(comment.getUser() != null ? comment.getUser().getName() : null);
        return commentDto;
    }

    public static Comment toComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        comment.setItem(commentDto.getItem() != null ? ItemMapper.toItem(commentDto.getItem()) : null);
        comment.setUser(commentDto.getUser() != null ? UserMapper.toUser(commentDto.getUser()) : null);
        comment.setCommentTime(commentDto.getCreated());
        return comment;
    }
}
