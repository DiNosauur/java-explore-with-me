package ru.practicum.ewm.participation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.participation.dto.ParticipationDto;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ParticipationController {
    private final ParticipationService service;

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public Collection<ParticipationDto> findEventParticipations(@PathVariable long userId,
                                                                @PathVariable long eventId) {
        return service.findEventParticipations(userId, eventId);
    }

    @GetMapping("/users/{userId}/requests")
    public Collection<ParticipationDto> findUserParticipations(@PathVariable long userId) {
        return service.findUserParticipations(userId);
    }

    @PostMapping("/users/{userId}/requests")
    public ResponseEntity<ParticipationDto> addRequest(@PathVariable long userId,
                                                       @RequestParam long eventId) {
        return new ResponseEntity<>(service.addRequest(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<ParticipationDto> delRequest(@PathVariable long userId,
                                                       @PathVariable long requestId) {
        return service.delRequest(userId, requestId)
                .map(delRequest -> new ResponseEntity<>(delRequest, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ResponseEntity<ParticipationDto> confirmRequest(@PathVariable long userId,
                                                           @PathVariable long eventId,
                                                           @PathVariable long reqId) {
        return service.confirmRequest(userId, eventId, reqId)
                .map(confirmRequest -> new ResponseEntity<>(confirmRequest, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ResponseEntity<ParticipationDto> rejectRequest(@PathVariable long userId,
                                                          @PathVariable long eventId,
                                                          @PathVariable long reqId) {
        return service.rejectRequest(userId, eventId, reqId)
                .map(rejectRequest -> new ResponseEntity<>(rejectRequest, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
