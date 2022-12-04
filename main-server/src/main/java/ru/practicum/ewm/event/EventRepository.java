package ru.practicum.ewm.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAllByInitiatorId(long initiatorId, Pageable pageable);

    @Query(" select e from Event e " +
            " where (upper(e.annotation) like upper(concat('%', :text, '%')) or " +
            "        upper(e.description) like upper(concat('%', :text, '%'))) " +
            "   and (substring(:mode, 1, 1) = '0' or e.categoryId in :categories) " +
            "   and (substring(:mode, 2, 1) = '0' or e.paid = :paid) " +
            "   and (substring(:mode, 3, 1) = '0' or e.eventDate >= :rangeStart) " +
            "   and (substring(:mode, 4, 1) = '0' or e.eventDate <= :rangeEnd) " +
            "   and (:onlyAvailable = false or e.participantLimit - e.confirmedRequests > 0) " +
            " order by e.eventDate desc ")
    Page<Event> findEvents(@Param("mode") String mode,
                           @Param("text") String text,
                           @Param("categories") List<Long> categories,
                           @Param("paid") Boolean paid,
                           @Param("rangeStart") LocalDateTime rangeStart,
                           @Param("rangeEnd") LocalDateTime rangeEnd,
                           @Param("onlyAvailable") Boolean onlyAvailable,
                           Pageable pageable);

    @Query(value = "select e from Event e " +
            " where (substring(:mode, 1, 1) = '0' or e.initiatorId in :users) " +
            "   and (substring(:mode, 2, 1) = '0' or e.state in :states) " +
            "   and (substring(:mode, 3, 1) = '0' or e.categoryId in :categories) " +
            "   and (substring(:mode, 4, 1) = '0' or e.eventDate >= :rangeStart) " +
            "   and (substring(:mode, 5, 1) = '0' or e.eventDate <= :rangeEnd) " +
            " order by e.eventDate desc ")
    Page<Event> adminFindEvents(@Param("mode") String mode,
                                @Param("users") List<Long> users,
                                @Param("states") List<EventState> states,
                                @Param("categories") List<Long> categories,
                                @Param("rangeStart") LocalDateTime rangeStart,
                                @Param("rangeEnd") LocalDateTime rangeEnd,
                                Pageable pageable);
}
