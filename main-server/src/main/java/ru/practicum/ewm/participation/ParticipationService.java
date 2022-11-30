package ru.practicum.ewm.participation;

import ru.practicum.ewm.participation.dto.ParticipationDto;

import java.util.Collection;
import java.util.Optional;

public interface ParticipationService {
    Collection<ParticipationDto> findEventParticipations(long userId, long eventId);

    Collection<ParticipationDto> findUserParticipations(long userId);

    ParticipationDto addRequest(long userId, long eventId);

    Optional<ParticipationDto> delRequest(long userId, long requestId);

    Optional<ParticipationDto> confirmRequest(long userId, long eventId, long reqId);

    Optional<ParticipationDto> rejectRequest(long userId, long eventId, long reqId);
}
