package ru.practicum.ewm.participation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    Collection<Participation> findAllByEventId(Long eventId);

    Collection<Participation> findAllByRequesterId(Long userId);
}
