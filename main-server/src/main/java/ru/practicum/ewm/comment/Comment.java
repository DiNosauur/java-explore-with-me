package ru.practicum.ewm.comment;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments", schema = "public")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // уникальный идентификатор комментария
    @Column(name = "event_id")
    private Long eventId; // идентификатор комментируемого события
    @Column(name = "commentator_id")
    private Long commentatorId; // идентификатоп комментатора
    @Column
    private String comment; // текст комментария
    @Column
    private LocalDateTime created; // Дата и время создания
    @Column
    private LocalDateTime published; // Дата и время публикации
    @Column
    private LocalDateTime updated; // Дата и время последнего изменения
    @Column(name = "comment_moderation")
    private Boolean commentModeration; // Нужна ли пре-модерация комментариев перед публикацией
    @Enumerated(EnumType.STRING)
    private CommentState state; // статус комментария
    @Column(name = "comment_id")
    private Long commentId; // ссылка на комментируемый комментарий
}
