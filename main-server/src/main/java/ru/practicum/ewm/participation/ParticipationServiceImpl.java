package ru.practicum.ewm.participation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.EventState;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.participation.dto.ParticipationDto;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {
    private final ParticipationRepository repository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private User validateUser(long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new NotFoundException(String.format("Пользователь (id = %s) не найден", userId));
        }
        return user.get();
    }

    private Event validateEvent(long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (!event.isPresent()) {
            throw new NotFoundException(String.format("Событие (id = %s) не найдено", eventId));
        }
        return event.get();
    }

    private void validateRequest(long userId, long eventId) {
        User user = validateUser(userId);
        Event event = validateEvent(eventId);
        if (event.getInitiatorId() != userId) {
            throw new ValidationException(String.format(
                    "Пользователь (id = %s) не является инициатором события (id = %s)", userId, eventId));
        }
    }

    @Override
    public Collection<ParticipationDto> findEventParticipations(long userId, long eventId) {
        log.info("Получение информации о запросах на участие в событии (eventId={}) текущего пользователя (users={})",
                eventId, userId);
        validateRequest(userId, eventId);
        return repository.findAllByEventId(eventId)
                .stream()
                .map(participation -> ParticipationMapper.toParticipationDto(participation))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ParticipationDto> findUserParticipations(long userId) {
        log.info("Получение информации о заявках на участие в чужих событиях текущего пользователя (users={})",
                userId);
        return repository.findAllByRequesterId(userId)
                .stream()
                .map(participation -> ParticipationMapper.toParticipationDto(participation))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationDto addRequest(long userId, long eventId) {
        log.info("Добавление запроса от текущего пользователя (users={}) на участие в событии (eventId={})",
                userId, eventId);
        ParticipationStatus status = validateAddRequest(userId, eventId);
        return ParticipationMapper.toParticipationDto(
                repository.save(ParticipationMapper.toParticipation(userId, eventId, status)));
    }

    @Transactional
    @Override
    public Optional<ParticipationDto> delRequest(long userId, long requestId) {
        log.info("Отмена пользователем (users={}) своего запроса (requestId={}) на участие в событии",
                userId, requestId);
        Optional<Participation> participation = validateCancelRequest(userId, requestId);
        if (participation.isPresent()) {
            participation.get().setStatus(ParticipationStatus.CANCELED);
            repository.save(participation.get());
            return Optional.of(ParticipationMapper.toParticipationDto(participation.get()));
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<ParticipationDto> confirmRequest(long userId, long eventId, long reqId) {
        log.info("Подтверждение чужой заявки (reqId={}) на участие в событии (eventId={})" +
                " текущего пользователя (users={})", reqId, eventId, userId);
        Optional<Participation> participation = validateConfirmRequest(userId, eventId, reqId);
        if (participation.isPresent()) {
            participation.get().setStatus(ParticipationStatus.CONFIRMED);
            repository.save(participation.get());
            return Optional.of(ParticipationMapper.toParticipationDto(participation.get()));
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<ParticipationDto> rejectRequest(long userId, long eventId, long reqId) {
        log.info("Отклонение чужой заявки (reqId={}) на участие в событии (eventId={})" +
                " текущего пользователя (users={})", reqId, eventId, userId);
        Optional<Participation> participation = validateConfirmRequest(userId, eventId, reqId);
        if (participation.isPresent()) {
            participation.get().setStatus(ParticipationStatus.REJECTED);
            repository.save(participation.get());
            return Optional.of(ParticipationMapper.toParticipationDto(participation.get()));
        }
        return Optional.empty();
    }

    private Optional<Participation> validateConfirmRequest(long userId, long eventId, long reqId) {
        validateUser(userId);
        Event event = validateEvent(eventId);
        if (event.getRequestModeration() &&
                event.getParticipantLimit() > 0 &&
                event.getParticipantLimit() - event.getConfirmedRequests() <= 0) {
            throw new ValidationException(String.format(
                    "нельзя подтвердить заявку (id = %s) на события (id = %s), т.к. " +
                            "лимит заявок для события исчерпан", reqId, eventId));
        }
        return repository.findById(reqId);
    }

    private Optional<Participation> validateCancelRequest(long userId, long requestId) {
        validateUser(userId);
        return repository.findById(requestId);
    }

    private ParticipationStatus validateAddRequest(long userId, long eventId) {
        validateUser(userId);
        Event event = validateEvent(eventId);
        if (event.getInitiatorId() == userId) {
            throw new ValidationException(String.format(
                    "Пользователь (id = %s) не может добавить запрос на участие в своём событии (id = %s)",
                    userId, eventId));
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException(String.format(
                    "нельзя участвовать в неопубликованном событии (id = %s)", eventId));
        }
        if (event.getRequestModeration() &&
                event.getParticipantLimit() > 0 &&
                event.getParticipantLimit() - event.getConfirmedRequests() <= 0) {
            throw new ValidationException(String.format(
                    "достигнут лимит запросов на участие в событии (id = %s)", eventId));
        }
        if (repository.findByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ValidationException(String.format(
                    "нельзя добавить повторный запрос от пользователя (id = %s) на участие в событии (id = %s)",
                    userId, eventId));
        }
        return ParticipationStatus.PENDING;
    }
}
