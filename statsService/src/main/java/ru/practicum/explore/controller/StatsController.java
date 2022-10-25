package ru.practicum.explore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.model.EndpointHit;
import ru.practicum.explore.model.ViewStats;
import ru.practicum.explore.service.StatsService;

import java.util.List;

@RestController
@RequestMapping()
public class StatsController {

    public final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/hit")
    public ResponseEntity<Object> postHit(@RequestBody EndpointHit endpointHit) {
        System.out.println("зашли в модуль статистики с хитом - "+endpointHit);
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(statsService.postHit(endpointHit), HttpStatus.OK);
        System.out.println("возвращаем "+responseEntity);
        return responseEntity;
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam(name = "uris")
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
