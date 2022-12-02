package ru.practicum.ewm.stat;

import ru.practicum.ewm.stat.dto.*;

import java.util.Collection;
import java.util.List;

public interface StatService {
    Collection<ViewStats> findHits(List<String> uris,
                                   String start,
                                   String end,
                                   Boolean unique);

    Stat addHit(StatDto statDto);
}
