package ru.practicum.explore.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.Mapper;
import ru.practicum.explore.model.EndpointHit;
import ru.practicum.explore.model.Hit;
import ru.practicum.explore.service.StatsService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping()
public class StatsController {

    private final StatsService statsService;
    private final Mapper mapper;

    @Autowired
    public StatsController(StatsService statsService, Mapper mapper) {
        this.statsService = statsService;
        this.mapper = mapper;
    }

    @PostMapping("/hit")
    public ResponseEntity<Object> postHit(@RequestBody EndpointHit endpointHit) {
        Hit hit = mapper.fromEndpointHitToHit(endpointHit);
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(statsService.postHit(hit), HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam(name = "uris")
                                           List<String> uris,
                                           @RequestParam(name = "unique", defaultValue = "false")
                                           boolean unique,
                                           @RequestParam(name = "start")
                                           String start,
                                           @RequestParam(name = "end")
                                           String end) {
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(statsService.getStats(uris, unique, start, end), HttpStatus.OK);
        return responseEntity;
    }


}
