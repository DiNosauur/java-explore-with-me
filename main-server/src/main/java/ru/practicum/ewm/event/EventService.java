package ru.practicum.ewm.event;

import ru.practicum.ewm.event.dto.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventService {
    EventFullDto saveEvent(NewEventDto eventDto, Long initiatorId);

    Optional<EventFullDto> updateEvent(UpdateEventRequestDto eventDto, Long initiatorId);

    Optional<EventFullDto> annulateEvent(Long initiatorId, Long eventId);

    Optional<EventFullDto> adminUpdateEvent(AdminUpdateEventRequestDto eventDto, Long eventId);

    Optional<EventFullDto> adminPublishEvent(Long eventId);

    Optional<EventFullDto> adminRejectEvent(Long eventId);

    Collection<EventFullDto> adminFindEvents(List<Long> users,
                                             List<EventState> states,
                                             List<Long> categories,
                                             String rangeStart,
                                             String rangeEnd,
                                             int from,
                                             int size);

    Collection<EventShortDto> findUserEvents(long initiatorId,
                                             int from,
                                             int size);

    Collection<EventShortDto> findEvents(String text,
                                         List<Long> categories,
                                         Boolean paid,
                                         String rangeStart,
                                         String rangeEnd,
                                         Boolean onlyAvailable,
                                         String sort,
                                         int from,
                                         int size);

    Optional<EventFullDto> getUserEvent(long initiatorId, long eventId);

    Optional<EventFullDto> getEvent(long id);
}
