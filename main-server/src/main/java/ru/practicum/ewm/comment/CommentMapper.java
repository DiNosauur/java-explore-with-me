package ru.practicum.ewm.comment;

import ru.practicum.ewm.comment.dto.*;

import java.time.LocalDateTime;

import static ru.practicum.ewm.comment.CommentState.*;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getEventId(),
                comment.getCommentatorId(),
                comment.getComment(),
                comment.getCreated(),
                comment.getPublished(),
                comment.getUpdated(),
                comment.getState(),
                comment.getCommentId());
    }

    public static Comment toComment(NewCommentDto commentDto,
                                    long userId,
                                    long eventId,
                                    Boolean commentModeration,
                                    CommentState state) {
        Comment comment = new Comment();
        comment.setEventId(eventId);
        comment.setCommentatorId(userId);
        comment.setComment(commentDto.getComment());
        comment.setCreated(LocalDateTime.now());
        if (state.equals(PUBLISHED)) {
            comment.setPublished(LocalDateTime.now());
        }
        comment.setCommentModeration(commentModeration);
        comment.setState(state);
        comment.setCommentId(commentDto.getCommentId());
        return comment;
    }

    public static Comment toComment(UpdCommentDto commentDto,
                                    CommentState state,
                                    Comment commentOld) {
        Comment comment = commentOld;
        comment.setComment(commentDto.getComment());
        comment.setUpdated(LocalDateTime.now());
        comment.setState(state);
        return comment;
    }
}
