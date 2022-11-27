package ru.practicum.ewm.participation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/participations")
public class ParticipationController {
    private final ParticipationService service;
}
