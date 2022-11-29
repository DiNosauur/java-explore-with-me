package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.dto.EventFullDto;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final CompilationEventsRepository compilationEventsRepository;
    private final EventService eventService;

    @Transactional
    @Override
    public CompilationDto saveCompilation(NewCompilationDto newCompilationDto) {
        log.info("Создание новой подборки событий {}", newCompilationDto);
        Compilation compilation = repository.save(CompilationMapper.toCompilation(newCompilationDto));
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilation.getId(), newCompilationDto);
        for (Long eventId : newCompilationDto.getEvents()) {
            Optional<EventFullDto> eventFullDto = eventService.getEvent(eventId);
            if (eventFullDto.isPresent()) {
                compilationEventsRepository.save(CompilationMapper.toCompilationEvents(compilation.getId(), eventId));
                compilationDto.getEvents().add(EventMapper.toEventShortDto(eventFullDto.get()));
            }
        }
        return compilationDto;
    }

    @Transactional
    @Override
    public boolean deleteCompilation(long compId) {
        log.info("Удаление подборки событий (compId={})", compId);
        Optional<Compilation> compilation = repository.findById(compId);
        if (compilation.isPresent()) {
            compilationEventsRepository.deleteAllByCompilationId(compId);
            repository.deleteById(compId);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean deleteCompilationEvent(long compId, long eventId) {
        log.info("Удаление события (eventId={}) из подборки (compId={})", eventId, compId);
        if (compilationEventsRepository.deleteByCompilationIdAndEventId(compId, eventId).isPresent()) {
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean addCompilationEvent(long compId, long eventId) {
        log.info("Добавление события (eventId={}) в подборку (compId={})", eventId, compId);
        Optional<Compilation> compilation = repository.findById(compId);
        if (compilation.isPresent()) {
            compilationEventsRepository.save(CompilationMapper.toCompilationEvents(compId, eventId));
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean pinCompilation(long compId, boolean pinned) {
        log.info("{} подборки (compId={}) на главной странице", pinned ? "Закрепление" : "Открепление", compId);
        Optional<Compilation> compilation = repository.findById(compId);
        if (compilation.isPresent()) {
            compilation.get().setPinned(pinned);
            repository.save(compilation.get());
            return true;
        }
        return false;
    }

    @Override
    public Optional<CompilationDto> findCompilation(long compId) {
        log.info("Получение подборки событий (compId={})", compId);
        Optional<Compilation> compilation = repository.findById(compId);
        if (compilation.isPresent()) {
            return Optional.of(toCompilationDto(compilation.get()));
        }
        return Optional.empty();
    }

    @Override
    public Collection<CompilationDto> findCompilations(Boolean pinned, int from, int size) {
        log.info("Получение подборок событий (pinned={})", pinned);
        int page = from / size;
        return repository.findAllByPinned(pinned, PageRequest.of(page, size))
                .stream()
                .map(compilation -> toCompilationDto(compilation))
                .collect(Collectors.toList());
    }

    private CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilation);
        Collection<CompilationEvents> compilationEvents =
                compilationEventsRepository.findAllByCompilationId(compilation.getId());
        for (CompilationEvents event : compilationEvents) {
            Optional<EventFullDto> eventFullDto = eventService.getEvent(event.getEventId());
            if (eventFullDto.isPresent()) {
                compilationDto.getEvents().add(EventMapper.toEventShortDto(eventFullDto.get()));
            }
        }
        return compilationDto;
    }

}
