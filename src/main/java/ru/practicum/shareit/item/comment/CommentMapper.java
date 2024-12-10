package ru.practicum.shareit.item.comment;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setItem(comment.getItem());
        commentDto.setUser(comment.getUser());
        commentDto.setCreated(comment.getCommentTime());
        commentDto.setAuthorName(comment.getUser().getName());
        return commentDto;
    }

    public static Comment toComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        comment.setItem(commentDto.getItem());
        comment.setUser(commentDto.getUser());
        comment.setCommentTime(commentDto.getCreated());
        return comment;
    }
}
