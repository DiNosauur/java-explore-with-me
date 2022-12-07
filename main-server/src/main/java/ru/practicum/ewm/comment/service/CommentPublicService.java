package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.CommentDto;

import java.util.Collection;

public interface CommentPublicService {
    Collection<CommentDto> getPublishedEventComments(Long eventId,
                                                     String rangeStart,
                                                     String rangeEnd,
                                                     int from,
                                                     int size);
}
