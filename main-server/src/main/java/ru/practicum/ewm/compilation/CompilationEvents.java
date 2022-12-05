package ru.practicum.ewm.compilation;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "compilation_events", schema = "public")
@Data
public class CompilationEvents {
    @Id
    @Column(name = "compilation_id")
    private Long compilationId; // Идентификатор подборки событий
    @Column(name = "event_id")
    private Long eventId; // Идентификатор события
}
