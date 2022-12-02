package ru.practicum.ewm.stat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stat.dto.StatDto;
import ru.practicum.ewm.stat.dto.ViewStats;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class StatController {
    private final StatService service;

    @GetMapping("/stats")
    public Collection<ViewStats> findHits(@RequestParam(required = false) String start,
                                          @RequestParam(required = false) String end,
                                          @RequestParam(required = false) List<String> uris,
                                          @RequestParam(defaultValue = "false") boolean unique
                                          ) {
        return service.findHits(uris, start, end, unique);
    }

    @PostMapping("/hit")
    public ResponseEntity<Stat> addHit(@RequestBody StatDto statDto) {
        return new ResponseEntity<>(service.addHit(statDto), HttpStatus.OK);
    }
}
