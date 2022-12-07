package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.*;

import java.util.Collection;
import java.util.Optional;

public interface CommentPrivateService {
    CommentDto saveComment(NewCommentDto commentDto, Long eventId, Long commentatorId);

    Optional<CommentDto> updateComment(UpdCommentDto commentDto, Long eventId, Long commentatorId);

    Optional<CommentDto> deleteComment(Long id, Long eventId, Long commentatorId);

    Collection<CommentDto> getUserComments(Long userId,
                                           int from,
                                           int size);

    Optional<CommentDto> getUserComment(Long userId, Long commentId);

    Collection<CommentDto> getUserCommentComments(Long userId,
                                                  Long commentId,
                                                  int from,
                                                  int size);
}
