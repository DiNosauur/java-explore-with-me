package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Validated
public class EventController {
    private final EventService service;

    @PutMapping("/admin/events/{eventId}")
    public ResponseEntity<EventFullDto> adminUpdateEvent(@PathVariable long eventId,
                                                         @RequestBody AdminUpdateEventRequestDto eventDto) {
        return service.adminUpdateEvent(eventDto, eventId)
                .map(updatedEvent -> new ResponseEntity<>(updatedEvent, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/admin/events/{eventId}/publish")
    public ResponseEntity<EventFullDto> adminPublishEvent(@PathVariable long eventId) {
        return service.adminPublishEvent(eventId)
                .map(updatedEvent -> new ResponseEntity<>(updatedEvent, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/admin/events/{eventId}/reject")
    public ResponseEntity<EventFullDto> adminRejectEvent(@PathVariable long eventId) {
        return service.adminRejectEvent(eventId)
                .map(updatedEvent -> new ResponseEntity<>(updatedEvent, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/admin/events")
    public Collection<EventFullDto> adminFindEvents(@RequestParam(required = false) List<Long> users,
                                                    @RequestParam(required = false) List<String> states,
                                                    @RequestParam(required = false) List<Long> categories,
                                                    @RequestParam(required = false) String rangeStart,
                                                    @RequestParam(required = false) String rangeEnd,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        return service.adminFindEvents(
                users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PostMapping("/users/{initiatorId}/events")
    public ResponseEntity<EventFullDto> createEvent(@PathVariable long initiatorId,
                                                    @Valid @RequestBody NewEventDto eventDto) {
        return new ResponseEntity<>(service.saveEvent(eventDto, initiatorId), HttpStatus.OK);
    }

    @PatchMapping("/users/{initiatorId}/events")
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable long initiatorId,
                                                    @RequestBody UpdateEventRequestDto eventDto) {
        return service.updateEvent(eventDto, initiatorId)
                .map(updatedEvent -> new ResponseEntity<>(updatedEvent, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/users/{initiatorId}/events/{eventId}")
    public ResponseEntity<EventFullDto> annulateEvent(@PathVariable long initiatorId,
                                                      @PathVariable long eventId) {
        return service.annulateEvent(initiatorId, eventId)
                .map(updatedEvent -> new ResponseEntity<>(updatedEvent, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/users/{initiatorId}/events")
    public Collection<EventShortDto> findEvents(@PathVariable long initiatorId,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        return service.findUserEvents(initiatorId, from, size);
    }

    @GetMapping("/users/{initiatorId}/events/{eventId}")
    public ResponseEntity<EventFullDto> findUserEventById(@PathVariable long initiatorId,
                                                          @PathVariable long eventId) {
        return service.getUserEvent(initiatorId, eventId)
                .map(event -> new ResponseEntity<>(event, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/events")
    public Collection<EventShortDto> findEvents(@RequestParam(required = false) String text,
                                                @RequestParam(required = false) List<Long> categories,
                                                @RequestParam(required = false) Boolean paid,
                                                @RequestParam(required = false) String rangeStart,
                                                @RequestParam(required = false) String rangeEnd,
                                                @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        return service.findEvents(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<EventFullDto> findEventById(@PathVariable long id) {
        return service.getEvent(id).map(event -> new ResponseEntity<>(event, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
