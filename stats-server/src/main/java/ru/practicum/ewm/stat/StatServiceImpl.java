package ru.practicum.ewm.stat;

import ru.practicum.ewm.stat.dto.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Collection;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatRepository repository;

    @Override
    public Collection<ViewStats> findHits(List<String> uris, String start, String end, Boolean unique) {
        log.info("Получение информации о количестве просмотров (uris={},start={},end={},unique={})",
                uris, start, end, unique);
        return unique
                ? repository.findUniqueHits(uris,
                LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                : repository.findHits(uris,
                LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @Transactional
    @Override
    public Stat addHit(StatDto statDto) {
        log.info("Сохранение информации о запросе {}", statDto.toString());
        Stat stat = StatMapper.toStat(statDto);
        return repository.save(stat);
    }
}
