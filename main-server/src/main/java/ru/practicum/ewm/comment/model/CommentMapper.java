package ru.practicum.ewm.comment.model;

import ru.practicum.ewm.comment.dto.*;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;
import java.util.Collection;

import static ru.practicum.ewm.comment.model.CommentState.*;

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
                                    long eventId,
                                    long userId,
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

    public static CommentShortDto toCommentShortDto(Comment comment,
                                                    Event event,
                                                    UserShortDto commentator,
                                                    Collection<CommentShortDto> commentShortDto) {
        return new CommentShortDto(
                comment.getId(),
                new CommentEventDto(event.getId(), event.getTitle(), event.getEventDate()),
                commentator,
                comment.getComment(),
                comment.getPublished(),
                commentShortDto);
    }
}
