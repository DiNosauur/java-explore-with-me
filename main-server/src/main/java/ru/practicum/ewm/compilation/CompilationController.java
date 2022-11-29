package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.*;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class CompilationController {
    private final CompilationService service;

    @PostMapping("/admin/compilations")
    public ResponseEntity<CompilationDto> createEvent(@RequestBody NewCompilationDto compilationDto) {
        return new ResponseEntity<>(service.saveCompilation(compilationDto), HttpStatus.OK);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public ResponseEntity<CompilationDto> deleteCompilation(@PathVariable long compId) {
        return service.deleteCompilation(compId)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    public ResponseEntity<CompilationDto> deleteCompilationEvent(@PathVariable long compId,
                                                                 @PathVariable long eventId) {
        return service.deleteCompilationEvent(compId, eventId)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    public ResponseEntity<CompilationDto> addCompilationEvent(@PathVariable long compId,
                                                              @PathVariable long eventId) {
        return service.addCompilationEvent(compId, eventId)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    public ResponseEntity<CompilationDto> unpinCompilation(@PathVariable long compId) {
        return service.pinCompilation(compId, false)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/admin/compilations/{compId}/pin")
    public ResponseEntity<CompilationDto> pinCompilation(@PathVariable long compId) {
        return service.pinCompilation(compId, true)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/compilations")
    public Collection<CompilationDto> findCompilations(@RequestParam(required = false) Boolean pinned,
                                                       @RequestParam(defaultValue = "0") int from,
                                                       @RequestParam(defaultValue = "10") int size) {
        return service.findCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public ResponseEntity<CompilationDto> findCompilation(@PathVariable long compId) {
        return service.findCompilation(compId).map(event -> new ResponseEntity<>(event, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
