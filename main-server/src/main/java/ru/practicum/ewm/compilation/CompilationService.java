package ru.practicum.ewm.compilation;

import ru.practicum.ewm.compilation.dto.*;

import java.util.Collection;
import java.util.Optional;

public interface CompilationService {
    CompilationDto saveCompilation(NewCompilationDto compilationDto);

    boolean deleteCompilation(long compId);

    boolean deleteCompilationEvent(long compId, long eventId);

    boolean addCompilationEvent(long compId, long eventId);

    boolean pinCompilation(long compId, boolean pinned);

    Collection<CompilationDto> findCompilations(Boolean pinned, int from, int size);

    Optional<CompilationDto> findCompilation(long compId);
}
