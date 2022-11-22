package ru.practicum.ewm.event;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events", schema = "public")
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // Уникальный идентификатор события
    @Column
    private String title; // Заголовок
    @Column
    private String annotation; // Краткое описание события
    @Column
    private String description; // Полное описание события
    @Column(name = "category_id")
    private Long categoryId; // Категория события
    @Column(name = "confirmed_requests")
    private Long confirmedRequests; // Количество одобренных заявок на участие в данном событии
    @Column(name = "created_on")
    private LocalDateTime createdOn; // Дата и время создания события
    @Column(name = "event_date")
    private LocalDateTime eventDate; // Дата и время на которые намечено событие
    @Column(name = "initiator_id")
    private Long initiatorId; // Инициатор события
    @Column(name = "location_id")
    private Long locationId; // Координаты события
    @Column
    private Boolean paid; // Нужно ли оплачивать участие
    @Column(name = "participant_limit")
    private Long participantLimit; // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    @Column(name = "published_on")
    private LocalDateTime publishedOn; // Дата и время публикации события
    @Column(name = "request_moderation")
    private Boolean requestModeration; // Нужна ли пре-модерация заявок на участие
    @Enumerated(EnumType.STRING)
    private EventState state; // статус события
}
