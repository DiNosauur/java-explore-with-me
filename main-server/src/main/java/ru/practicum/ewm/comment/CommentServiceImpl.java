package ru.practicum.ewm.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdCommentDto;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.participation.Participation;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.ewm.participation.ParticipationRepository;

import java.util.Collection;
import java.util.Optional;

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

    private void validateComment(long userId, long eventId) {
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

    private Comment validateComment(long id, long userId, long eventId) {
        validateComment(userId, eventId);
        Optional<Comment> comment = repository.findById(id);
        if (comment.isEmpty()) {
            throw new NotFoundException(String.format("Комментарий (id = %s) не найден", id));
        }
        if (comment.get().getCommentatorId() != userId) {
            throw new ValidationException(String.format(
                    "Пользователь (id = %s) не является автором комментария (id = %s)",
                    userId, id));
        }
        if (comment.get().getState().equals(CommentState.PUBLISHED)) {
            throw new ValidationException(String.format(
                    "Комментарий (id = %s) уже опубликован", id));
        }
        return comment.get();
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
        Comment comment = validateComment(commentDto.getId(), commentatorId, eventId);
        comment.setComment(commentDto.getComment());
        repository.save(comment);
        return Optional.of(CommentMapper.toCommentDto(comment));
    }

    @Transactional
    @Override
    public boolean deleteComment(Long id, Long eventId, Long commentatorId) {
        return false;
    }

    @Transactional
    @Override
    public Optional<CommentDto> adminUpdateComment(UpdCommentDto commentDto, Long id) {
        return Optional.empty();
    }

    @Transactional
    @Override
    public boolean adminDeleteComment(Long id) {
        return false;
    }

    @Transactional
    @Override
    public Optional<CommentDto> adminPublishComment(Long id) {
        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<CommentDto> adminCancelComment(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<CommentDto> adminFindComment(Long id) {
        return Optional.empty();
    }

    @Override
    public Collection<CommentDto> getEventComments(Long eventId) {
        return null;
    }

    @Override
    public Collection<CommentDto> getCommentComments(Long commentId) {
        return null;
    }
}
