package ru.practicum.ewm.comment;

import ru.practicum.ewm.comment.dto.*;

import java.util.Collection;
import java.util.Optional;

public interface CommentService {
    CommentDto saveComment(NewCommentDto commentDto, Long eventId, Long commentatorId);

    Optional<CommentDto> updateComment(UpdCommentDto commentDto, Long eventId, Long commentatorId);

    Optional<CommentDto> deleteComment(Long id, Long eventId, Long commentatorId);

    Optional<CommentDto> adminUpdateComment(UpdCommentDto commentDto);

    Optional<CommentDto> adminDeleteComment(Long id);

    Optional<CommentDto> adminPublishComment(Long id);

    Optional<CommentDto> adminCancelComment(Long id);

    Optional<CommentDto> adminFindComment(Long id);

    Collection<CommentDto> adminFindComments(Long id,
                                             int from,
                                             int size);

    Collection<CommentDto> adminGetEventComments(Long eventId,
                                                 int from,
                                                 int size);

    Collection<CommentDto> getPublishedEventComments(Long eventId,
                                                     String rangeStart,
                                                     String rangeEnd,
                                                     int from,
                                                     int size);

    Collection<CommentDto> getUserComments(Long userId,
                                           int from,
                                           int size);

    Optional<CommentDto> getUserComment(Long userId, Long commentId);

    Collection<CommentDto> getUserCommentComments(Long userId,
                                                  Long commentId,
                                                  int from,
                                                  int size);
}
