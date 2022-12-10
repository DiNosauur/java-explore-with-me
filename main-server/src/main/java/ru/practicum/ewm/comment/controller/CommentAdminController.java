package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.UpdCommentDto;
import ru.practicum.ewm.comment.service.CommentAdminService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
@Validated
public class CommentAdminController {
    private final CommentAdminService service;

    @PatchMapping
    public ResponseEntity<CommentDto> updateComment(@Valid @RequestBody UpdCommentDto commentDto) {
        return service.adminUpdateComment(commentDto)
                .map(updatedComment -> new ResponseEntity<>(updatedComment, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{commId}")
    public ResponseEntity<CommentDto> deleteComment(@PathVariable long commId) {
        return service.adminDeleteComment(commId)
                .map(delComment -> new ResponseEntity<>(delComment, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/publish")
    public Collection<CommentDto> publishComment(@RequestParam List<Long> ids) {
        return service.adminPublishComment(ids);
    }

    @PatchMapping("/cancel")
    public Collection<CommentDto> cancelComment(@RequestParam List<Long> ids) {
        return service.adminCancelComment(ids);
    }

    @GetMapping("/comment/{commId}")
    public ResponseEntity<CommentDto> findAdminComment(@PathVariable long commId) {
        return service.adminFindComment(commId)
                .map(delComment -> new ResponseEntity<>(delComment, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{commId}")
    public Collection<CommentDto> findAdminComments(@PathVariable long commId,
                                                    @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                    @Positive @RequestParam(defaultValue = "20") int size) {
        return service.adminFindComments(commId, from, size);
    }

    @GetMapping("/events/{eventId}")
    public Collection<CommentDto> findAdminEventComments(@PathVariable long eventId,
                                                         @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                         @Positive @RequestParam(defaultValue = "20") int size) {
        return service.adminGetEventComments(eventId, from, size);
    }
}