package ru.practicum.explore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explore.model.EndpointHit;
import ru.practicum.explore.model.ViewStats;
import ru.practicum.explore.storage.StatsRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class StatsService {

    private final StatsRepository statsRepository;

    @Autowired
    public StatsService(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    public EndpointHit postHit(EndpointHit endpointHit) {
        System.out.println("зашли в последний рубеж - сохранение в сторадж " + endpointHit);
        statsRepository.save(endpointHit);
        log.warn("хит запощен");
        EndpointHit backHit = statsRepository.findById(endpointHit.getId()).get();
        return endpointHit;
    }

    public List<ViewStats> getStats(List<String> uris, boolean unique, String start, String end) {
        log.warn("статистика получена");
        List<EndpointHit> list = statsRepository.findAll();
        List<ViewStats> resultList = new ArrayList<>();
        resultList.add(new ViewStats());
        return resultList;
    }
}
