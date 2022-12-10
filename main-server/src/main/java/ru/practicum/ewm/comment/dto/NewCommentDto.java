package ru.practicum.ewm.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class NewCommentDto {
    @NotBlank(message = "Comment is required")
    @Size(min = 20, max = 7000)
    private String comment; // текст комментария
    private Long commentId; // ссылка на комментируемый комментарий
}
