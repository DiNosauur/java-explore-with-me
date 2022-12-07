package ru.practicum.ewm.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.dto.*;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.CommentMapper;
import ru.practicum.ewm.comment.model.CommentState;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.participation.Participation;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.ewm.participation.ParticipationRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentPrivateServiceImpl implements CommentPrivateService {
    private final CommentRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ParticipationRepository participationRepository;

    private User validateUser(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь (id = %s) не найден", userId)));
    }

    private Event validateEvent(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Событие (id = %s) не найдено", eventId)));
    }

    private void validateCommentEvent(long userId, long eventId) {
        validateUser(userId);
        Event event = validateEvent(eventId);
        if (event.getInitiatorId() != userId) {
            Participation part = participationRepository.findByRequesterIdAndEventId(userId, eventId).orElseThrow(
                    () -> new ValidationException(String.format(
                            "Пользователь (id = %s) не является инициатором или участником события (id = %s)",
                            userId, eventId)));
        }
    }

    private Comment validateComment(long id, long userId) {
        Comment comment = repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Комментарий (id = %s) не найден", id)));
        if (comment.getCommentatorId() != userId) {
            throw new ValidationException(String.format(
                    "Пользователь (id = %s) не является автором комментария (id = %s)", userId, id));
        }
        return comment;
    }

    private Comment validateComment(long id, long userId, long eventId, boolean isUpd) {
        validateCommentEvent(userId, eventId);
        Comment comment = validateComment(id, userId);
        if (isUpd && comment.getState().equals(CommentState.PUBLISHED)) {
            throw new ValidationException(String.format(
                    "Комментарий (id = %s) уже опубликован", id));
        }
        return comment;
    }

    @Transactional
    @Override
    public CommentDto saveComment(NewCommentDto commentDto, Long eventId, Long commentatorId) {
        log.info("Добавление комментария {} к событию (id={}) от пользователя (id={})",
                commentDto.toString(), eventId, commentatorId);
        validateCommentEvent(commentatorId, eventId);
        Comment comment = repository.save(CommentMapper.toComment(
                commentDto, eventId, commentatorId, true, CommentState.PENDING));
        return CommentMapper.toCommentDto(comment);
    }

    @Transactional
    @Override
    public Optional<CommentDto> updateComment(UpdCommentDto commentDto, Long eventId, Long commentatorId) {
        log.info("Изменение комментария (id={}) к событию (id={}) от пользователя (id={})",
                commentDto.getId(), eventId, commentatorId);
        Comment comment = validateComment(commentDto.getId(), commentatorId, eventId, true);
        comment.setComment(commentDto.getComment());
        comment.setUpdated(LocalDateTime.now());
        repository.save(comment);
        return Optional.of(CommentMapper.toCommentDto(comment));
    }

    @Transactional
    @Override
    public Optional<CommentDto> deleteComment(Long id, Long eventId, Long commentatorId) {
        log.info("Удаление комментария (id={}) к событию (id={}) от пользователя (id={})",
                id, eventId, commentatorId);
        Comment comment = validateComment(id, commentatorId, eventId, false);
        comment.setState(CommentState.CANCELED);
        comment.setUpdated(LocalDateTime.now());
        repository.save(comment);
        return Optional.of(CommentMapper.toCommentDto(comment));
    }

    @Override
    public Collection<CommentDto> getUserComments(Long userId,
                                                  int from,
                                                  int size) {
        log.info("Получение информации о комментариях пользователя (id={})", userId);
        int page = from / size;
        return repository.findAllByCommentatorIdOrderByPublished(userId,
                PageRequest.of(page, size))
                .stream()
                .map(comment -> CommentMapper.toCommentDto(comment))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<CommentDto> getUserCommentComments(Long userId,
                                                         Long commentId,
                                                         int from,
                                                         int size) {
        log.info("Получение информации о комментариях к комментарию (id={}) пользователем (id={})",
                commentId, userId);
        validateComment(commentId, userId);
        int page = from / size;
        return repository.findAllByIdOrCommentIdOrderByPublished(commentId, commentId,
                PageRequest.of(page, size))
                .stream()
                .map(comment -> CommentMapper.toCommentDto(comment))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CommentDto> getUserComment(Long userId, Long commentId) {
        log.info("Получение информации о комментарии (id={}) пользователем (id={})",
                commentId, userId);
        Comment comment = validateComment(commentId, userId);
        return Optional.of(CommentMapper.toCommentDto(comment));
    }
}
