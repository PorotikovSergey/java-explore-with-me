package ru.practicum.explore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explore.model.EndpointHit;
import ru.practicum.explore.model.ViewStats;
import ru.practicum.explore.storage.StatsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        System.out.println("возвращаем из стораджа после сохранения хита "+backHit);
        return backHit;
    }

    public List<ViewStats> getStats(List<String> uris, boolean unique, String start, String end) {
        System.out.println("зашли в последний рубеж - получение из строраджа по параметрам uris-"+ uris +", unique-"+ unique +", start-"+ start +", end-"+ end);
        System.out.println("вот такие хиты все "+statsRepository.findAll());
        List<EndpointHit> list = statsRepository.findAllByUriIn(uris);
        System.out.println("вот такие хиты нашлись "+list);
        List<ViewStats> resultList = list.stream().map(this::getViewStats).collect(Collectors.toList());
        log.warn("статистика получена");
        System.out.println("возвращаем из стораджа после получения статистики и маппинга "+resultList);
        return resultList;
    }

    private ViewStats getViewStats(EndpointHit endpointHit) {
        ViewStats viewStats = new ViewStats();
        viewStats.setApp(endpointHit.getApp());
        viewStats.setUri(endpointHit.getUri());
        return viewStats;
    }
}
