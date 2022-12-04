package ru.practicum.ewm.stat;

import ru.practicum.ewm.stat.dto.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatRepository extends JpaRepository<Stat, Long> {
    @Query(" select new ru.practicum.ewm.stat.dto.ViewStats(s.app, s.uri, count(distinct s.ip)) from Stat s " +
            " where s.uri in (?1) " +
            "   and s.timestamp between ?2 and ?3 " +
            " group by s.app, s.uri ")
    Collection<ViewStats> findUniqueHits(List<String> uris,
                                         LocalDateTime start,
                                         LocalDateTime end);

    @Query(" select new ru.practicum.ewm.stat.dto.ViewStats(s.app, s.uri, count(s.ip)) from Stat s " +
            " where s.uri in (?1) " +
            "   and s.timestamp between ?2 and ?3 " +
            " group by s.app, s.uri ")
    Collection<ViewStats> findHits(List<String> uris,
                                   LocalDateTime start,
                                   LocalDateTime end);
}
