package ru.practicum.ewm.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentShortDto;
import ru.practicum.ewm.comment.model.CommentMapper;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.UserMapper;
import ru.practicum.ewm.user.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentPublicServiceImpl implements CommentPublicService {
    private final CommentRepository repository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private Event validateEvent(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Событие (id = %s) не найдено", eventId)));
    }

    @Override
    public Collection<CommentDto> getPublishedEventComments(Long eventId,
                                                            String rangeStart,
                                                            String rangeEnd,
                                                            int from,
                                                            int size) {
        log.info("Получение информации об опубликованных комментариях к событию (id={})", eventId);
        Event event = validateEvent(eventId);
        int page = from / size;
        return repository.findPublishedComments(eventId,
                rangeStart == null ?
                        event.getCreatedOn() :
                        LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                rangeEnd == null ?
                        LocalDateTime.now() :
                        LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                PageRequest.of(page, size))
                .stream()
                .map(comment -> CommentMapper.toCommentDto(comment))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<CommentShortDto> getPublishedComments(Long eventId,
                                                            String rangeStart,
                                                            String rangeEnd,
                                                            int from,
                                                            int size) {
        Event event = validateEvent(eventId);
        LocalDateTime start = rangeStart == null ? event.getCreatedOn() :
                LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime end = rangeEnd == null ? LocalDateTime.now() :
                LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return getPublishedComments(event, eventId, null, start, end, from, size);
    }

    private Collection<CommentShortDto> getPublishedComments(Event event,
                                                             Long eventId,
                                                             Long commId,
                                                             LocalDateTime rangeStart,
                                                             LocalDateTime rangeEnd,
                                                             int from,
                                                             int size) {
        int page = from / size;
        if (eventId != null && commId == null) {
            log.info("Поиск комментариев на событие (id={})", eventId);
            return repository.findPublishedEventComments(
                    event.getId(), rangeStart, rangeEnd, PageRequest.of(page, size))
                    .stream()
                    .map(comment -> CommentMapper.toCommentShortDto(comment, event,
                            UserMapper.toUserShortDto(userRepository.findById(comment.getCommentatorId()).get()),
                            getPublishedComments(event, null, comment.getId(), rangeStart, rangeEnd, 0, 100)))
                    .collect(Collectors.toList());
        } else if (eventId == null && commId != null) {
            log.info("Поиск комментариев на комментарий (id={})", commId);
            return repository.findPublishedCommentComments(
                    commId, rangeStart, rangeEnd, PageRequest.of(page, size))
                    .stream()
                    .map(comment -> CommentMapper.toCommentShortDto(comment, event,
                            UserMapper.toUserShortDto(userRepository.findById(comment.getCommentatorId()).get()),
                            getPublishedComments(event, null, comment.getId(), rangeStart, rangeEnd, 0, 100)))
                    .collect(Collectors.toList());
        }
        return List.of(new CommentShortDto());
    }
}
