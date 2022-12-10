package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.UpdCommentDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CommentAdminService {
    Optional<CommentDto> adminUpdateComment(UpdCommentDto commentDto);

    Optional<CommentDto> adminDeleteComment(Long id);

    Collection<CommentDto> adminPublishComment(List<Long> ids);

    Collection<CommentDto> adminCancelComment(List<Long> ids);

    Optional<CommentDto> adminFindComment(Long id);

    Collection<CommentDto> adminFindComments(Long id, int from, int size);

    Collection<CommentDto> adminGetEventComments(Long eventId, int from, int size);
}
