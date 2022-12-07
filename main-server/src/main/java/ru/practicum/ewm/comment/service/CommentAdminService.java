package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.UpdCommentDto;

import java.util.Collection;
import java.util.Optional;

public interface CommentAdminService {
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
}
