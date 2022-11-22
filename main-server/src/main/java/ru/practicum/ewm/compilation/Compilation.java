package ru.practicum.ewm.compilation;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "compilations", schema = "public")
@Data
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // Уникальный идентификатор подборки событий
    @Column
    private String title; // Заголовок
    @Column
    private Boolean pinned; // Закреплена ли подборка на главной странице сайта
}
