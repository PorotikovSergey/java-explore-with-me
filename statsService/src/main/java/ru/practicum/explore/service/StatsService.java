package ru.practicum.explore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explore.model.Hit;
import ru.practicum.explore.model.ViewStats;
import ru.practicum.explore.storage.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    public Hit postHit(Hit hit) {
        statsRepository.save(hit);
        Hit backHit = statsRepository.findById(hit.getId()).get();
        return backHit;
    }

    public List<ViewStats> getStats(List<String> uris, boolean unique, String start, String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        LocalDateTime startTime;
        LocalDateTime endTime;

        try {
            startTime = LocalDateTime.parse(start, formatter);
            endTime = LocalDateTime.parse(end, formatter);
        } catch (DateTimeParseException e) {
            startTime = LocalDateTime.parse(start, formatter1);
            endTime = LocalDateTime.parse(end, formatter1);
        }

        List<Hit> list = statsRepository.findAllByUriInAndAndTimestampBetween(uris, startTime, endTime);
        return list.stream().map(this::getViewStats).collect(Collectors.toList());
    }

    private ViewStats getViewStats(Hit hit) {
        ViewStats viewStats = new ViewStats();
        viewStats.setApp(hit.getApp());
        viewStats.setUri(hit.getUri());
        return viewStats;
    }
}
