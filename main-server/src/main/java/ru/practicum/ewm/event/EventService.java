package ru.practicum.ewm.event;

import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewm.event.dto.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventService {
    EventFullDto saveEvent(NewEventDto eventDto, Long initiatorId, Long userId);

    Optional<EventFullDto> updateEvent(UpdateEventRequestDto eventDto, Long initiatorId, Long userId);

    Optional<EventFullDto> annulateEvent(Long initiatorId, Long eventId, Long userId);

    Optional<EventFullDto> adminUpdateEvent(AdminUpdateEventRequestDto eventDto, Long eventId, Long userId);

    Optional<EventFullDto> adminPublishEvent(Long eventId, Long userId);

    Optional<EventFullDto> adminRejectEvent(Long eventId, Long userId);

    Collection<EventFullDto> adminFindEvents(long userId,
                                             List<Long> users,
                                             List<String> states,
                                             List<Long> categories,
                                             LocalDateTime rangeStart,
                                             LocalDateTime rangeEnd,
                                             int from,
                                             int size);

    Collection<EventShortDto> findUserEvents(long userId,
                                             long initiatorId,
                                             int from,
                                             int size);

    Collection<EventShortDto> findEvents(long userId,
                                         String text,
                                         List<Long> categories,
                                         Boolean paid,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd,
                                         Boolean onlyAvailable,
                                         String sort,
                                         int from,
                                         int size);

    Optional<EventFullDto> getUserEvent(long userId, long initiatorId, long eventId);

    Optional<EventFullDto> getEvent(long id, long userId);
}
