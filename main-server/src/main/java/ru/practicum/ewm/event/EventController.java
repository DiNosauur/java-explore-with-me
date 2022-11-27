package ru.practicum.ewm.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class EventController {
    private final EventService service;

    @PutMapping("/admin/events/{eventId}")
    public ResponseEntity<EventFullDto> adminUpdateEvent(@RequestHeader("X-Sharer-User-Id") long userId,
                                                         @PathVariable long eventId,
                                                         @RequestBody AdminUpdateEventRequestDto eventDto) {
        return service.adminUpdateEvent(eventDto, eventId, userId)
                .map(updatedEvent -> new ResponseEntity<>(updatedEvent, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/admin/events/{eventId}/publish")
    public ResponseEntity<EventFullDto> adminPublishEvent(@RequestHeader("X-Sharer-User-Id") long userId,
                                                          @PathVariable long eventId) {
        return service.adminPublishEvent(eventId, userId)
                .map(updatedEvent -> new ResponseEntity<>(updatedEvent, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/admin/events/{eventId}/reject")
    public ResponseEntity<EventFullDto> adminRejectEvent(@RequestHeader("X-Sharer-User-Id") long userId,
                                                         @PathVariable long eventId) {
        return service.adminRejectEvent(eventId, userId)
                .map(updatedEvent -> new ResponseEntity<>(updatedEvent, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/admin/events")
    public Collection<EventFullDto> adminFindEvents(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @RequestParam(required = false) List<Long> users,
                                                    @RequestParam(required = false) List<String> states,
                                                    @RequestParam(required = false) List<Long> categories,
                                                    @RequestParam(required = false) LocalDateTime rangeStart,
                                                    @RequestParam(required = false) LocalDateTime rangeEnd,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        return service.adminFindEvents(
                userId, users, states, categories, rangeStart, rangeEnd, from, size);
    }


    @PostMapping("/users/{initiatorId}/events")
    public ResponseEntity<EventFullDto> createEvent(//@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @PathVariable long initiatorId,
                                                    @RequestBody NewEventDto eventDto) {
        return new ResponseEntity<>(service.saveEvent(eventDto, initiatorId, initiatorId), HttpStatus.OK);
    }

    @PatchMapping("/users/{initiatorId}/events")
    public ResponseEntity<EventFullDto> updateEvent(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @PathVariable long initiatorId,
                                                    @RequestBody UpdateEventRequestDto eventDto) {
        return service.updateEvent(eventDto, initiatorId, userId)
                .map(updatedEvent -> new ResponseEntity<>(updatedEvent, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/users/{initiatorId}/events/{eventId}")
    public ResponseEntity<EventFullDto> annulateEvent(@RequestHeader("X-Sharer-User-Id") long userId,
                                                      @PathVariable long initiatorId,
                                                      @PathVariable long eventId) {
        return service.annulateEvent(initiatorId, eventId, userId)
                .map(updatedEvent -> new ResponseEntity<>(updatedEvent, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/users/{initiatorId}/events")
    public Collection<EventShortDto> findEvents(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @PathVariable long initiatorId,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        return service.findUserEvents(userId, initiatorId, from, size);
    }

    @GetMapping("/users/{initiatorId}/events/{eventId}")
    public ResponseEntity<EventFullDto> findUserEventById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                          @PathVariable long initiatorId,
                                                          @PathVariable long eventId) {
        return service.getUserEvent(userId, initiatorId, eventId)
                .map(event -> new ResponseEntity<>(event, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/events")
    public Collection<EventShortDto> findEvents(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestParam(required = false) String text,
                                                @RequestParam(required = false) List<Long> categories,
                                                @RequestParam(required = false) Boolean paid,
                                                @RequestParam(required = false) LocalDateTime rangeStart,
                                                @RequestParam(required = false) LocalDateTime rangeEnd,
                                                @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        return service.findEvents(
                userId, text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> findEventById(@PathVariable long id,
                                                      @RequestHeader("X-Sharer-User-Id") long userId) {
        return service.getEvent(id, userId).map(event -> new ResponseEntity<>(event, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
