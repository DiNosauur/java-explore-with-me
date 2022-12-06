package ru.practicum.ewm.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UpdCommentDto {
    private Long id; // уникальный идентификатор комментария
    @NotBlank(message = "Comment is required")
    private String comment; // текст комментария
}
