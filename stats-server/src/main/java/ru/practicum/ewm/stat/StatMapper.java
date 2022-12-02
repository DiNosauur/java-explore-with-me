package ru.practicum.ewm.stat;

import ru.practicum.ewm.stat.dto.StatDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatMapper {
    public static Stat toStat(StatDto statDto) {
        Stat stat = new Stat();
        stat.setId(statDto.getId());
        stat.setApp(statDto.getApp());
        stat.setUri(statDto.getUri());
        stat.setIp(statDto.getIp());
        stat.setTimestamp(LocalDateTime.parse(statDto.getTimestamp(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return stat;
    }
}
