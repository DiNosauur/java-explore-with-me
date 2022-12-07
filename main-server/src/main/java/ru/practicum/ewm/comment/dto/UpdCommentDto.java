package ru.practicum.ewm.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UpdCommentDto {
    @NotNull
    private Long id; // уникальный идентификатор комментария
    @NotBlank(message = "Comment is required")
    private String comment; // текст комментария
}
