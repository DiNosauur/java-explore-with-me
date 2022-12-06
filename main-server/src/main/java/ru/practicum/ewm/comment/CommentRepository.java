package ru.practicum.ewm.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository  extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByEventIdAndStateOrderByPublished(Long eventId, String state,  Pageable pageable);

    Page<Comment> findAllByCommentIdAndStateOrderByPublished(Long commentId, String state,  Pageable pageable);
}
