package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.user.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private EventFullDto toEventFullDto(Event event) {
        return EventMapper.toEventFullDto(event,
                categoryRepository.findById(event.getCategoryId()).get(),
                userRepository.findById(event.getInitiatorId()).get(),
                locationRepository.findById(event.getLocationId()).get(),
                null);
    }

    @Transactional
    @Override
    public EventFullDto saveEvent(NewEventDto eventDto, Long initiatorId) {
        log.info("Добавление события {} от пользователя (id={})", eventDto.toString(), initiatorId);
        Optional<Location> location = locationRepository.findByLatAndLon(
                eventDto.getLocation().getLat(),
                eventDto.getLocation().getLon());
        if (location.isEmpty()) {
            location = Optional.of(locationRepository.save(EventMapper.toLocation(eventDto.getLocation())));
        }

        Event event = repository.save(EventMapper.toEvent(eventDto, initiatorId, location.get().getId()));
        return toEventFullDto(event);
    }

    @Transactional
    @Override
    public Optional<EventFullDto> updateEvent(UpdateEventRequestDto eventDto, Long initiatorId) {
        log.info("Изменение события {} от пользователя (id={})", eventDto.toString(), initiatorId);
        Optional<Event> eventOld = repository.findById(eventDto.getEventId());
        if (eventOld.isPresent()) {
            Event event = repository.save(EventMapper.toEvent(eventDto, eventOld.get()));
            return Optional.of(toEventFullDto(event));
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public Optional<EventFullDto> adminUpdateEvent(AdminUpdateEventRequestDto eventDto, Long eventId) {
        log.info("Изменение события {} администратором", eventDto.toString());
        Optional<Event> eventOld = repository.findById(eventId);
        if (eventOld.isPresent()) {
            Event event = repository.save(EventMapper.toEvent(eventDto, eventOld.get()));
            return Optional.of(toEventFullDto(event));
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public Optional<EventFullDto> adminPublishEvent(Long eventId) {
        log.info("Публикация события (id={}) администратором", eventId);
        Optional<Event> eventOld = repository.findById(eventId);
        if (eventOld.isPresent()) {
            eventOld.get().setState(EventState.PUBLISHED);
            eventOld.get().setPublishedOn(LocalDateTime.now());
            Event event = repository.save(eventOld.get());
            return Optional.of(toEventFullDto(event));
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public Optional<EventFullDto> adminRejectEvent(Long eventId) {
        log.info("Отклонение события (id={}) администратором", eventId);
        Optional<Event> eventOld = repository.findById(eventId);
        if (eventOld.isPresent()) {
            eventOld.get().setState(EventState.CANCELED);
            Event event = repository.save(eventOld.get());
            return Optional.of(toEventFullDto(event));
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public Optional<EventFullDto> annulateEvent(Long initiatorId, Long eventId) {
        log.info("Аннулирование события (id={}) от пользователя (id={})", eventId, initiatorId);
        Optional<Event> eventOld = repository.findById(eventId);
        if (eventOld.isPresent()) {
            eventOld.get().setState(EventState.CANCELED);
            Event event = repository.save(eventOld.get());
            return Optional.of(toEventFullDto(event));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Collection<EventShortDto> findUserEvents(long initiatorId, int from, int size) {
        log.info("Получение информации о событиях пользователя (id={})", initiatorId);
        int page = from / size;
        return repository.findAllByInitiatorId(initiatorId,
                PageRequest.of(page, size))
                .stream()
                .map(event -> EventMapper.toEventShortDto(event,
                        categoryRepository.findById(event.getCategoryId()).get(),
                        userRepository.findById(event.getInitiatorId()).get(),
                        null))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<EventShortDto> findEvents(String text,
                                                List<Long> categories,
                                                Boolean paid,
                                                String rangeStart,
                                                String rangeEnd,
                                                Boolean onlyAvailable,
                                                String sort,
                                                int from,
                                                int size) {
        log.info("Получение информации о событиях " +
                        "(text={},categories={},paid={},rangeStart={},rangeEnd={},onlyAvailable={},sort={})",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort);
        int page = from / size;
        return repository.findEvents(text, categories, paid,
                rangeStart == null ? null : LocalDateTime.parse(rangeStart,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                rangeEnd == null ? null : LocalDateTime.parse(rangeEnd,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                onlyAvailable,
                PageRequest.of(page, size))
                .stream()
                .map(event -> EventMapper.toEventShortDto(event,
                        categoryRepository.findById(event.getCategoryId()).get(),
                        userRepository.findById(event.getInitiatorId()).get(),
                        null))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<EventFullDto> adminFindEvents(List<Long> users,
                                                    List<String> states,
                                                    List<Long> categories,
                                                    String rangeStart,
                                                    String rangeEnd,
                                                    int from,
                                                    int size) {
        log.info("Получение информации о событиях (users={},states={},categories={},rangeStart={},rangeEnd={})",
                users, states, categories, rangeStart, rangeEnd);
        int page = from / size;
        return repository.adminFindEvents(
                users,
                states,
                categories,
                rangeStart == null ? null : LocalDateTime.parse(rangeStart,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                rangeEnd == null ? null : LocalDateTime.parse(rangeEnd,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                PageRequest.of(page, size))
                .stream()
                .map(event -> toEventFullDto(event))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EventFullDto> getUserEvent(long initiatorId, long eventId) {
        log.info("Получение информации о событии (id={}) пользователя (id={}) им самим", eventId, initiatorId);
        Optional<Event> event = repository.findById(eventId);
        if (event.isPresent()) {
            return Optional.of(toEventFullDto(event.get()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<EventFullDto> getEvent(long id) {
        log.info("Получение информации о событии (id={})", id);
        Optional<Event> event = repository.findById(id);
        if (event.isPresent()) {
            return Optional.of(toEventFullDto(event.get()));
        } else {
            return Optional.empty();
        }
    }
}
