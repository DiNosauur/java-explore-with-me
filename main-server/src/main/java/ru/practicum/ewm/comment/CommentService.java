package ru.practicum.ewm.comment;

import ru.practicum.ewm.comment.dto.*;

import java.util.Collection;
import java.util.Optional;

public interface CommentService {
    CommentDto saveComment(NewCommentDto commentDto, Long eventId, Long commentatorId);

    Optional<CommentDto> updateComment(UpdCommentDto commentDto, Long eventId, Long commentatorId);

    boolean deleteComment(Long id, Long eventId, Long commentatorId);

    Optional<CommentDto> adminUpdateComment(UpdCommentDto commentDto, Long id);

    boolean adminDeleteComment(Long id);

    Optional<CommentDto> adminPublishComment(Long id);

    Optional<CommentDto> adminCancelComment(Long id);

    Optional<CommentDto> adminFindComment(Long id);

    Collection<CommentDto> getEventComments(Long eventId);

    Collection<CommentDto> getCommentComments(Long commentId);
}
