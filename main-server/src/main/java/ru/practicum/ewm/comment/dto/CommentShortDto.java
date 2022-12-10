package ru.practicum.ewm.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentShortDto {
    private Long id; // уникальный идентификатор комментария
    private CommentEventDto event; // событие
    private UserShortDto commentator; // комментатор
    private String comment; // текст комментария
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime published; // Дата и время публикации
    private Collection<CommentShortDto> comments; // комментарии, ссылающиеся на данный комментарий
}
