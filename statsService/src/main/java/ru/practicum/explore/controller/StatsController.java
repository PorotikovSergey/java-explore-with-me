package ru.practicum.explore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.model.Hit;
import ru.practicum.explore.model.ViewStats;
import ru.practicum.explore.service.StatsService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping()
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    public Hit postHit(@RequestBody Hit hit) {
        return statsService.postHit(hit);
    }

    @GetMapping("/stats")
    public Collection<ViewStats> getStats(@RequestParam(name = "uris")
                                          List<String> uris,
                                          @RequestParam(name = "unique", defaultValue = "false")
                                          boolean unique,
                                          @RequestParam(name = "start")
                                          String start,
                                          @RequestParam(name = "end")
                                          String end) {
        return statsService.getStats(uris, unique, start, end);
    }


}
