package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentShortDto;
import ru.practicum.ewm.comment.service.CommentPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentPublicController {
    private final CommentPublicService service;

    @GetMapping("/events/{eventId}")
    public Collection<CommentDto> findEventComments(@PathVariable long eventId,
                                                    @RequestParam(required = false) String rangeStart,
                                                    @RequestParam(required = false) String rangeEnd,
                                                    @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                    @Positive @RequestParam(defaultValue = "20") int size) {
        return service.getPublishedEventComments(eventId, rangeStart, rangeEnd, from, size);
    }

    @GetMapping
    public Collection<CommentShortDto> findComments(@RequestParam Long eventId,
                                                    @RequestParam(required = false) String rangeStart,
                                                    @RequestParam(required = false) String rangeEnd,
                                                    @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                    @Positive @RequestParam(defaultValue = "20") int size) {
        return service.getPublishedComments(eventId, rangeStart, rangeEnd, from, size);
    }
}
