package ru.practicum.ewm.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.ewm.comment.model.CommentState;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    private Long id; // уникальный идентификатор комментария
    private Long eventId; // идентификатор комментируемого события
    private Long commentatorId; // идентификатор комментатора
    private String comment; // текст комментария
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created; // Дата и время создания
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime published; // Дата и время публикации
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated; // Дата и время последнего изменения
    private CommentState state; // статус комментария
    private Long commentId; // ссылка на комментируемый комментарий
}
