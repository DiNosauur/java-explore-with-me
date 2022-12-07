package ru.practicum.ewm.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.dto.*;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.participation.Participation;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.ewm.participation.ParticipationRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ParticipationRepository participationRepository;

    private User validateUser(long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new NotFoundException(String.format("Пользователь (id = %s) не найден", userId));
        }
        return user.get();
    }

    private Event validateEvent(long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (!event.isPresent()) {
            throw new NotFoundException(String.format("Событие (id = %s) не найдено", eventId));
        }
        return event.get();
    }

    private void validateCommentEvent(long userId, long eventId) {
        validateUser(userId);
        Event event = validateEvent(eventId);
        if (event.getInitiatorId() != userId) {
            Optional<Participation> participation =
                    participationRepository.findByRequesterIdAndEventId(userId, eventId);
            if (participation.isEmpty()) {
                throw new ValidationException(String.format(
                        "Пользователь (id = %s) не является инициатором или участником события (id = %s)",
                        userId, eventId));
            }
        }
    }

    private Comment validateComment(long id, long userId) {
        Optional<Comment> comment = repository.findById(id);
        if (comment.isEmpty()) {
            throw new NotFoundException(String.format("Комментарий (id = %s) не найден", id));
        }
        if (comment.get().getCommentatorId() != userId) {
            throw new ValidationException(String.format(
                    "Пользователь (id = %s) не является автором комментария (id = %s)",
                    userId, id));
        }
        return comment.get();
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
        validateComment(commentatorId, eventId);
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

    @Transactional
    @Override
    public Optional<CommentDto> adminUpdateComment(UpdCommentDto commentDto) {
        log.info("Изменение комментария (id={}) администратором", commentDto.getId());
        Optional<Comment> comment = repository.findById(commentDto.getId());
        if (comment.isPresent()) {
            comment.get().setComment(commentDto.getComment());
            comment.get().setUpdated(LocalDateTime.now());
            repository.save(comment.get());
            return Optional.of(CommentMapper.toCommentDto(comment.get()));
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<CommentDto> adminDeleteComment(Long id) {
        log.info("Удаление комментария (id={}) администратором", id);
        Optional<Comment> comment = repository.findById(id);
        if (comment.isPresent()) {
            comment.get().setState(CommentState.CANCELED);
            comment.get().setUpdated(LocalDateTime.now());
            repository.save(comment.get());
            return Optional.of(CommentMapper.toCommentDto(comment.get()));
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<CommentDto> adminPublishComment(Long id) {
        log.info("Опубликование комментария (id={}) администратором", id);
        Optional<Comment> comment = repository.findById(id);
        if (comment.isPresent()) {
            if (comment.get().getState().equals(CommentState.PENDING)) {
                comment.get().setState(CommentState.PUBLISHED);
                comment.get().setPublished(LocalDateTime.now());
                comment.get().setUpdated(LocalDateTime.now());
                repository.save(comment.get());
                return Optional.of(CommentMapper.toCommentDto(comment.get()));
            }
            throw new ValidationException(String.format(
                    "Комментарий (id = %s) не в статусе ожидания", id));
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<CommentDto> adminCancelComment(Long id) {
        log.info("Отмена комментария (id={}) администратором", id);
        Optional<Comment> comment = repository.findById(id);
        if (comment.isPresent()) {
            comment.get().setState(CommentState.CANCELED);
            comment.get().setUpdated(LocalDateTime.now());
            repository.save(comment.get());
            return Optional.of(CommentMapper.toCommentDto(comment.get()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<CommentDto> adminFindComment(Long id) {
        log.info("Поиск комментария (id={}) администратором", id);
        Optional<Comment> comment = repository.findById(id);
        if (comment.isPresent()) {
            return Optional.of(CommentMapper.toCommentDto(comment.get()));
        }
        return Optional.empty();
    }

    @Override
    public Collection<CommentDto> adminFindComments(Long id,
                                                    int from,
                                                    int size) {
        log.info("Получение информации о комментариях к комментарию (id={})", id);
        int page = from / size;
        return repository.findAllByIdOrCommentIdOrderByPublished(id, id,
                PageRequest.of(page, size))
                .stream()
                .map(comment -> CommentMapper.toCommentDto(comment))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<CommentDto> adminGetEventComments(Long eventId,
                                                        int from,
                                                        int size) {
        log.info("Получение информации о комментариях к событию (id={})", eventId);
        int page = from / size;
        return repository.findAllByEventIdOrderByPublished(eventId,
                PageRequest.of(page, size))
                .stream()
                .map(comment -> CommentMapper.toCommentDto(comment))
                .collect(Collectors.toList());
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
        return repository.findPublishedEventComments(eventId,
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
