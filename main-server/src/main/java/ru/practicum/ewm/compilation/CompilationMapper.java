package ru.practicum.ewm.compilation;

import ru.practicum.ewm.compilation.dto.*;

import java.util.ArrayList;

public class CompilationMapper {
    public static Compilation toCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(compilationDto.getTitle());
        compilation.setPinned(compilationDto.getPinned() == null ? false : compilationDto.getPinned());
        return compilation;
    }

    public static CompilationEvents toCompilationEvents(Long compilationId, Long eventId) {
        CompilationEvents compilationEvents = new CompilationEvents();
        compilationEvents.setCompilationId(compilationId);
        compilationEvents.setEventId(eventId);
        return compilationEvents;
    }

    public static CompilationDto toCompilationDto(Long compilationId, NewCompilationDto compilationDto) {
        return new CompilationDto(
                compilationId,
                new ArrayList<>(),
                compilationDto.getTitle(),
                compilationDto.getPinned() == null ? false : compilationDto.getPinned()
        );
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getId(),
                new ArrayList<>(),
                compilation.getTitle(),
                compilation.getPinned()
        );
    }
}
