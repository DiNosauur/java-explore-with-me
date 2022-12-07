package ru.practicum.ewm.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.comment.model.Comment;

import java.time.LocalDateTime;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByEventIdOrderByPublished(Long eventId, Pageable pageable);

    Page<Comment> findAllByCommentatorIdOrderByPublished(Long userId, Pageable pageable);

    Page<Comment> findAllByIdOrCommentIdOrderByPublished(Long id, Long commentId, Pageable pageable);

    @Query(value = " with recursive comms (id, path, level ) " +
            "as ( select c.id, " +
            "            to_char(c.published, 'yyyy-mm-dd HH24:mi:ss') ||'->'|| cast (c.id as varchar(50)) as path, 1 " +
            "       from comments c " +
            "      where c.event_id = :eventId " +
            "        and c.published between :rangeStart and :rangeEnd " +
            "        and c.comment_id is null " +
            "        and c.state = 'PUBLISHED' " +
            "      union all " +
            "     select c.id, cast (comms.path ||'->'|| c.id as varchar(50)) as path, level + 1 " +
            "       from comments c inner join comms on (c.comment_id = comms.id and c.state = 'PUBLISHED')) " +
            "select c.* from comments c inner join comms on (c.id = comms.id) order by comms.path ",
            nativeQuery = true)
    Page<Comment> findPublishedEventComments(@Param("eventId") Long eventId,
                                             @Param("rangeStart") LocalDateTime rangeStart,
                                             @Param("rangeEnd") LocalDateTime rangeEnd,
                                             Pageable pageable);
}
