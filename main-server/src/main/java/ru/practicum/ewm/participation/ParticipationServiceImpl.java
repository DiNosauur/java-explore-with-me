package ru.practicum.ewm.participation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.participation.dto.ParticipationDto;
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

    @Override
    public Collection<ParticipationDto> findEventParticipations(long userId, long eventId) {
        log.info("Получение информации о запросах на участие в событии (eventId={}) текущего пользователя (users={})",
                eventId, userId);
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
        //если для события лимит заявок равен 0 или отключена пре-модерация заявок,
        // то подтверждение заявок не требуется
        //нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие
        //если при подтверждении данной заявки, лимит заявок для события исчерпан,
        // то все неподтверждённые заявки необходимо отклонить
        return repository.findById(reqId);
    }

    private Optional<Participation> validateCancelRequest(long userId, long requestId) {
        return repository.findById(requestId);
    }

    private ParticipationStatus validateAddRequest(long userId, long eventId) {
        //нельзя добавить повторный запрос
        //инициатор события не может добавить запрос на участие в своём событии
        //нельзя участвовать в неопубликованном событии
        //если у события достигнут лимит запросов на участие - необходимо вернуть ошибку
        //если для события отключена пре-модерация запросов на участие, то запрос должен
        // автоматически перейти в состояние подтвержденного
        return ParticipationStatus.PENDING;
    }
}
