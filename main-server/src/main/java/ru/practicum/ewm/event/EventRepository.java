package ru.practicum.ewm.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAllByInitiatorId(long initiatorId, Pageable pageable);

    @Query(" select e from Event e " +
            " where (upper(e.annotation) like upper(concat('%', ?1, '%')) or " +
            "        upper(e.description) like upper(concat('%', ?1, '%'))) " +
            "   and (?2 is null or e.categoryId in (?2)) " +
            "   and (?3 is null or e.paid = ?2) " +
            "   and (?4 is null or e.eventDate >= ?4) " +
            "   and (?5 is null or e.eventDate <= ?5) " +
            "   and (?6 = false or e.participantLimit - e.confirmedRequests > 0) ")
    Page<Event> findEvents(String text,
                           List<Long> categories,
                           Boolean paid,
                           LocalDateTime rangeStart,
                           LocalDateTime rangeEnd,
                           Boolean onlyAvailable,
                           Pageable pageable);

    @Query(" select e from Event e " +
            " where (?1 is null or e.initiatorId in (?1)) " +
            "   and (?2 is null or e.state in (?2)) " +
            "   and (?3 is null or e.categoryId in (?3)) " +
            "   and (?4 is null or e.eventDate >= ?4) " +
            "   and (?5 is null or e.eventDate <= ?5) ")
    Page<Event> adminFindEvents(List<Long> users,
                                List<String> states,
                                List<Long> categories,
                                LocalDateTime rangeStart,
                                LocalDateTime rangeEnd,
                                Pageable pageable);
}
