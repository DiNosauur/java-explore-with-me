package ru.practicum.ewm.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Validated
public class CommentController {
    private final CommentService service;

    @PostMapping("/users/{userId}/events/{eventId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable long userId,
                                                    @PathVariable long eventId,
                                                    @Valid @RequestBody NewCommentDto commentDto) {
        return new ResponseEntity<>(service.saveComment(commentDto, eventId, userId), HttpStatus.OK);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/comments")
    public ResponseEntity<CommentDto> updateComment(@PathVariable long userId,
                                                    @PathVariable long eventId,
                                                    @Valid @RequestBody UpdCommentDto commentDto) {
        return service.updateComment(commentDto, eventId, userId)
                .map(updatedComment -> new ResponseEntity<>(updatedComment, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/users/{userId}/events/{eventId}/comments/{commId}")
    public ResponseEntity<CommentDto> annulateComment(@PathVariable long userId,
                                                      @PathVariable long eventId,
                                                      @PathVariable long commId) {
        return service.deleteComment(commId, eventId, userId)
                .map(delComment -> new ResponseEntity<>(delComment, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/users/{userId}/comments")
    public Collection<CommentDto> findUserComments(@PathVariable long userId,
                                                   @RequestParam(defaultValue = "0") int from,
                                                   @RequestParam(defaultValue = "10") int size) {
        return service.getUserComments(userId, from, size);
    }

    @GetMapping("/users/{userId}/comments/{commId}")
    public Collection<CommentDto> findUserCommentComments(@PathVariable long userId,
                                                          @PathVariable long commId,
                                                          @RequestParam(defaultValue = "0") int from,
                                                          @RequestParam(defaultValue = "10") int size) {
        return service.getUserCommentComments(userId, commId, from, size);
    }

    @GetMapping("/users/{userId}/comment/{commId}")
    public ResponseEntity<CommentDto> findUserComment(@PathVariable long userId,
                                                      @PathVariable long commId) {
        return service.getUserComment(userId, commId)
                .map(delComment -> new ResponseEntity<>(delComment, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/admin/comments")
    public ResponseEntity<CommentDto> updateComment(@Valid @RequestBody UpdCommentDto commentDto) {
        return service.adminUpdateComment(commentDto)
                .map(updatedComment -> new ResponseEntity<>(updatedComment, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/admin/comments/{commId}")
    public ResponseEntity<CommentDto> deleteComment(@PathVariable long commId) {
        return service.adminDeleteComment(commId)
                .map(delComment -> new ResponseEntity<>(delComment, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/admin/comments/{commId}/publish")
    public ResponseEntity<CommentDto> publishComment(@PathVariable long commId) {
        return service.adminPublishComment(commId)
                .map(updatedComment -> new ResponseEntity<>(updatedComment, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/admin/comments/{commId}/cancel")
    public ResponseEntity<CommentDto> cancelComment(@PathVariable long commId) {
        return service.adminCancelComment(commId)
                .map(updatedComment -> new ResponseEntity<>(updatedComment, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/admin/comment/{commId}")
    public ResponseEntity<CommentDto> findAdminComment(@PathVariable long commId) {
        return service.adminFindComment(commId)
                .map(delComment -> new ResponseEntity<>(delComment, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/admin/comments/{commId}")
    public Collection<CommentDto> findAdminComments(@PathVariable long commId,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        return service.adminFindComments(commId, from, size);
    }

    @GetMapping("/admin/events/{eventId}/comments")
    public Collection<CommentDto> findAdminEventComments(@PathVariable long eventId,
                                                         @RequestParam(defaultValue = "0") int from,
                                                         @RequestParam(defaultValue = "10") int size) {
        return service.adminGetEventComments(eventId, from, size);
    }

    @GetMapping("/events/{eventId}/comments")
    public Collection<CommentDto> findEventComments(@PathVariable long eventId,
                                                    @RequestParam(required = false) String rangeStart,
                                                    @RequestParam(required = false) String rangeEnd,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        return service.getPublishedEventComments(eventId, rangeStart, rangeEnd, from, size);
    }
}
