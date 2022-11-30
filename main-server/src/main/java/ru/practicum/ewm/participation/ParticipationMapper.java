package ru.practicum.ewm.participation;

import ru.practicum.ewm.participation.dto.ParticipationDto;

import java.time.LocalDateTime;

public class ParticipationMapper {
    public static ParticipationDto toParticipationDto(Participation participation) {
        return new ParticipationDto(
                participation.getId(),
                participation.getCreated(),
                participation.getEventId(),
                participation.getRequesterId(),
                participation.getStatus()
        );
    }

    public static Participation toParticipation(long userId, long eventId, ParticipationStatus status) {
        Participation participation = new Participation();
        participation.setRequesterId(userId);
        participation.setEventId(eventId);
        participation.setCreated(LocalDateTime.now());
        participation.setStatus(status);
        return participation;
    }
}