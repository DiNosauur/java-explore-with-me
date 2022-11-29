package ru.practicum.ewm.compilation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface CompilationEventsRepository extends JpaRepository<CompilationEvents, Long> {
    Collection<CompilationEvents> findAllByCompilationId(long compId);

    Collection<CompilationEvents> deleteAllByCompilationId(long compId);

    Optional<CompilationEvents> deleteByCompilationIdAndEventId(long compId, long eventId);
}
