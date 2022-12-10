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

    private Comment validateComment(long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Комментарий (id = %s) не найден", id)));
    }

    private Comment validateComment(long id, long userId, boolean isUpd) {
        Comment comment = validateComment(id);
        if (comment.getCommentatorId() != userId) {
            throw new ValidationException(String.format(
                    "Пользователь (id = %s) не является автором комментария (id = %s)", userId, id));
        }
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
        if (commentDto.getCommentId() != null) {
            validateComment(commentDto.getCommentId());
        }
        Comment comment = repository.save(CommentMapper.toComment(
                commentDto, eventId, commentatorId, true, CommentState.PENDING));
        return CommentMapper.toCommentDto(comment);
    }

    @Transactional
    @Override
    public Optional<CommentDto> updateComment(UpdCommentDto commentDto, Long commentatorId) {
        log.info("Изменение комментария (id={}) пользователем (id={})",
                commentDto.getId(), commentatorId);
        Comment comment = validateComment(commentDto.getId(), commentatorId, true);
        comment.setComment(commentDto.getComment());
        comment.setUpdated(LocalDateTime.now());
        repository.save(comment);
        return Optional.of(CommentMapper.toCommentDto(comment));
    }

    @Transactional
    @Override
    public Optional<CommentDto> deleteComment(Long id, Long commentatorId) {
        log.info("Удаление комментария (id={}) пользователем (id={})",
                id, commentatorId);
        Comment comment = validateComment(id, commentatorId, false);
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
    public Optional<CommentDto> getUserComment(Long userId, Long commentId) {
        log.info("Получение информации о комментарии (id={}) пользователем (id={})",
                commentId, userId);
        Comment comment = validateComment(commentId, userId, false);
        return Optional.of(CommentMapper.toCommentDto(comment));
    }
}
