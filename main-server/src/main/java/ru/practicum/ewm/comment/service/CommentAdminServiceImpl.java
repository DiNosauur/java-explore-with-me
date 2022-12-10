package ru.practicum.ewm.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.UpdCommentDto;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.CommentMapper;
import ru.practicum.ewm.comment.model.CommentState;
import ru.practicum.ewm.comment.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentAdminServiceImpl implements CommentAdminService {
    private final CommentRepository repository;

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
            log.info("Удалено {} комментариев",
                    repository.deleteComments(comment.get().getId()));
            return Optional.of(CommentMapper.toCommentDto(comment.get()));
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public Collection<CommentDto> adminPublishComment(List<Long> ids) {
        log.info("Опубликование комментариев (ids={}) администратором", ids);
        Collection<CommentDto> comments = new ArrayList<>();
        for (Long id : ids) {
            Optional<Comment> comment = repository.findById(id);
            if (comment.isPresent()) {
                if (!comment.get().getState().equals(CommentState.PUBLISHED)) {
                    comment.get().setState(CommentState.PUBLISHED);
                    comment.get().setPublished(LocalDateTime.now());
                    comment.get().setUpdated(LocalDateTime.now());
                    repository.save(comment.get());
                }
                comments.add(CommentMapper.toCommentDto(comment.get()));
            }
        }
        return comments;
    }

    @Transactional
    @Override
    public Collection<CommentDto> adminCancelComment(List<Long> ids) {
        log.info("Отмена комментариев (ids={}) администратором", ids);
        Collection<CommentDto> comments = new ArrayList<>();
        for (Long id : ids) {
            Optional<Comment> comment = repository.findById(id);
            if (comment.isPresent()) {
                if (!comment.get().getState().equals(CommentState.CANCELED)) {
                    comment.get().setState(CommentState.CANCELED);
                    comment.get().setUpdated(LocalDateTime.now());
                    repository.save(comment.get());
                }
                comments.add(CommentMapper.toCommentDto(comment.get()));
            }
        }
        return comments;
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
    public Collection<CommentDto> adminFindComments(Long id, int from, int size) {
        log.info("Получение информации о комментариях к комментарию (id={})", id);
        int page = from / size;
        return repository.findAllByIdOrCommentIdOrderByPublished(id, id,
                PageRequest.of(page, size))
                .stream()
                .map(comment -> CommentMapper.toCommentDto(comment))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<CommentDto> adminGetEventComments(Long eventId, int from, int size) {
        log.info("Получение информации о комментариях к событию (id={})", eventId);
        int page = from / size;
        return repository.findAllByEventIdOrderByPublished(eventId,
                PageRequest.of(page, size))
                .stream()
                .map(comment -> CommentMapper.toCommentDto(comment))
                .collect(Collectors.toList());
    }
}
