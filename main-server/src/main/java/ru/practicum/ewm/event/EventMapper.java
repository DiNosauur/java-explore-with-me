package ru.practicum.ewm.event;

import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserMapper;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
                event.getConfirmedRequests(),
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
                event.getConfirmedRequests(),
                views
        );
    }

    public static EventShortDto toEventShortDto(EventFullDto eventFullDto) {
        return new EventShortDto(
                eventFullDto.getId(),
                eventFullDto.getTitle(),
                eventFullDto.getAnnotation(),
                eventFullDto.getCategory(),
                eventFullDto.getEventDate(),
                eventFullDto.getInitiator(),
                eventFullDto.getPaid(),
                eventFullDto.getConfirmedRequests(),
                eventFullDto.getViews()
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
        event.setPaid(eventDto.getPaid() == null ? false : eventDto.getPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit() == null ? 0 : eventDto.getParticipantLimit());
        event.setRequestModeration(eventDto.getRequestModeration() == null ? true : eventDto.getRequestModeration());
        event.setState(EventState.PENDING);
        return event;
    }

    public static Event toEvent(UpdateEventRequestDto eventDto, Event eventOld) {
        Event event = eventOld;
        event.setTitle(eventDto.getTitle() == null ? eventOld.getTitle() : eventDto.getTitle());
        event.setAnnotation(eventDto.getAnnotation() == null ? eventOld.getAnnotation() : eventDto.getAnnotation());
        event.setDescription(eventDto.getDescription() == null ? eventOld.getDescription() : eventDto.getDescription());
        event.setCategoryId(eventDto.getCategory() == null ? eventOld.getCategoryId() : eventDto.getCategory());
        event.setEventDate(eventDto.getEventDate() == null ? eventOld.getEventDate() :
                LocalDateTime.parse(eventDto.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        event.setPaid(eventDto.getPaid() == null ? eventOld.getPaid() : eventDto.getPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit() == null
                ? eventOld.getParticipantLimit() : eventDto.getParticipantLimit());
        event.setRequestModeration(true);
        event.setState(EventState.PENDING);
        return event;
    }

    public static Event toEvent(AdminUpdateEventRequestDto eventDto, Event eventOld) {
        Event event = eventOld;
        event.setTitle(eventDto.getTitle() == null ? eventOld.getTitle() : eventDto.getTitle());
        event.setAnnotation(eventDto.getAnnotation() == null ? eventOld.getAnnotation() : eventDto.getAnnotation());
        event.setDescription(eventDto.getDescription() == null ? eventOld.getDescription() : eventDto.getDescription());
        event.setCategoryId(eventDto.getCategory() == null ? eventOld.getCategoryId() : eventDto.getCategory());
        event.setEventDate(eventDto.getEventDate() == null ? eventOld.getEventDate() :
                LocalDateTime.parse(eventDto.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        event.setPaid(eventDto.getPaid() == null ? eventOld.getPaid() : eventDto.getPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit() == null
                ? eventOld.getParticipantLimit() : eventDto.getParticipantLimit());
        event.setRequestModeration(eventDto.getRequestModeration() == null
                ? eventOld.getRequestModeration() : eventDto.getRequestModeration());
        return event;
    }

    public static EndpointHit toEndpointHit(String uri) {
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            ip = "localhost";
        }
        return new EndpointHit(0L, "ewm-main-server", uri, ip,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    public static ViewStats toViewStats(String uri) {
        return new ViewStats("ewm-main-server", uri, 0L);
    }
}
