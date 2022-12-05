package ru.practicum.ewm.stat;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stats", schema = "public")
@Data
public class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // Уникальный идентификатор
    @Column
    private String app; // Название сервиса для которого записывается информация
    @Column
    private String uri; // URI для которого был осуществлен запрос
    @Column
    private String ip; // IP-адрес пользователя, осуществившего запрос
    @Column
    private LocalDateTime timestamp; // Дата и время, когда был совершен запрос к эндпоинту
}