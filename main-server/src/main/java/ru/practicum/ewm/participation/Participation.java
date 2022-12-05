package ru.practicum.ewm.participation;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participations", schema = "public")
@Data
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // Уникальный идентификатор заявки на участие в событии
    @Column
    private LocalDateTime created; // Дата и время создания заявки
    @Column(name = "event_id")
    private Long eventId; // Идентификатор события
    @Column(name = "requester_id")
    private Long requesterId; // Идентификатор пользователя, отправившего заявку
    @Enumerated(EnumType.STRING)
    private ParticipationStatus status; // Статус заявки
}
