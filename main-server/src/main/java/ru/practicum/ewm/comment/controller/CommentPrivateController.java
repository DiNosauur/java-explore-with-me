package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.service.CommentPrivateService;
import ru.practicum.ewm.comment.dto.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
@Validated
public class CommentPrivateController {
    private final CommentPrivateService service;

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@PathVariable long userId,
                                                    @RequestParam Long eventId,
                                                    @Valid @RequestBody NewCommentDto commentDto) {
        return new ResponseEntity<>(service.saveComment(commentDto, eventId, userId), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<CommentDto> updateComment(@PathVariable long userId,
                                                    @Valid @RequestBody UpdCommentDto commentDto) {
        return service.updateComment(commentDto, userId)
                .map(updComment -> new ResponseEntity<>(updComment, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{commId}")
    public ResponseEntity<CommentDto> annulateComment(@PathVariable long userId,
                                                      @PathVariable long commId) {
        return service.deleteComment(commId, userId)
                .map(delComment -> new ResponseEntity<>(delComment, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public Collection<CommentDto> findUserComments(@PathVariable long userId,
                                                   @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                   @Positive @RequestParam(defaultValue = "10") int size) {
        return service.getUserComments(userId, from, size);
    }

    @GetMapping("/{commId}")
    public ResponseEntity<CommentDto> findUserComment(@PathVariable long userId,
                                                      @PathVariable long commId) {
        return service.getUserComment(userId, commId)
                .map(comment -> new ResponseEntity<>(comment, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
