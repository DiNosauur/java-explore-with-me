package ru.practicum.ewm.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.ewm.comment.CommentState;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    private Long id; // уникальный идентификатор комментария
    private Long eventId; // идентификатор комментируемого события
    private Long commentatorId; // идентификатор комментатора
    private String comment; // текст комментария
    private LocalDateTime created; // Дата и время создания
    private LocalDateTime published; // Дата и время публикации
    private LocalDateTime updated; // Дата и время последнего изменения
    private CommentState state; // статус комментария
    private Long commentId; // ссылка на комментируемый комментарий
}
