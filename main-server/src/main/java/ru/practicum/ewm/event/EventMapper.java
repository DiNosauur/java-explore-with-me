package ru.practicum.ewm.event;

import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventMapper {
    public static EventFullDto toEventFullDto(Event event,
                                              Category category,
                                              User initiator,
                                              Location location,
                                              Long views) {
        return new EventFullDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                event.getDescription(),
                CategoryMapper.toCategoryDto(category),
                event.getCreatedOn(),
                event.getEventDate(),
                UserMapper.toUserShortDto(initiator),
                toLocationDto(location),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                views
        );
    }

    public static EventShortDto toEventShortDto(Event event,
                                                Category category,
                                                User initiator,
                                                Long views) {
        return new EventShortDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(category),
                event.getEventDate(),
                UserMapper.toUserShortDto(initiator),
                event.getPaid(),
                views
        );
    }

    public static LocationDto toLocationDto(Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }

    public static Location toLocation(LocationDto locationDto) {
        Location location = new Location();
        location.setLon(locationDto.getLon());
        location.setLat(locationDto.getLat());
        return location;
    }

    public static Event toEvent(NewEventDto eventDto, long userId, long locationId) {
        Event event = new Event();
        event.setTitle(eventDto.getTitle());
        event.setAnnotation(eventDto.getAnnotation());
        event.setDescription(eventDto.getDescription());
        event.setCategoryId(eventDto.getCategory());
        event.setConfirmedRequests(0L);
        event.setCreatedOn(LocalDateTime.now());
        event.setEventDate(LocalDateTime.parse(eventDto.getEventDate(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        event.setInitiatorId(userId);
        event.setLocationId(locationId);
        event.setPaid(eventDto.getPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit());
        event.setRequestModeration(eventDto.getRequestModeration());
        if (eventDto.getRequestModeration()) {
            event.setState(EventState.PENDING);
        } else {
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        }
        return event;
    }

    public static Event toEvent(UpdateEventRequestDto eventDto, Event eventOld) {
        Event event = new Event();
        event.setTitle(eventDto.getTitle() == null ? eventOld.getTitle() : eventDto.getTitle());
        event.setAnnotation(eventDto.getAnnotation() == null ? eventOld.getAnnotation() : eventDto.getAnnotation());
        event.setDescription(eventDto.getDescription() == null ? eventOld.getDescription() : eventDto.getDescription());
        event.setCategoryId(eventDto.getCategory() == null ? eventOld.getCategoryId() : eventDto.getCategory());
        event.setEventDate(eventDto.getEventDate() == null ? eventOld.getEventDate() : eventDto.getEventDate());
        event.setPaid(eventDto.getPaid() == null ? eventOld.getPaid() : eventDto.getPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit() == null
                ? eventOld.getParticipantLimit() : eventDto.getParticipantLimit());
        event.setRequestModeration(true);
        event.setState(EventState.PENDING);
        return event;
    }

    public static Event toEvent(AdminUpdateEventRequestDto eventDto, Event eventOld) {
        Event event = new Event();
        event.setTitle(eventDto.getTitle() == null ? eventOld.getTitle() : eventDto.getTitle());
        event.setAnnotation(eventDto.getAnnotation() == null ? eventOld.getAnnotation() : eventDto.getAnnotation());
        event.setDescription(eventDto.getDescription() == null ? eventOld.getDescription() : eventDto.getDescription());
        event.setCategoryId(eventDto.getCategory() == null ? eventOld.getCategoryId() : eventDto.getCategory());
        event.setEventDate(eventDto.getEventDate() == null ? eventOld.getEventDate() : eventDto.getEventDate());
        event.setPaid(eventDto.getPaid() == null ? eventOld.getPaid() : eventDto.getPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit() == null
                ? eventOld.getParticipantLimit() : eventDto.getParticipantLimit());
        event.setRequestModeration(eventDto.getRequestModeration() == null
                ? eventOld.getRequestModeration() : eventDto.getRequestModeration());
        return event;
    }
}
