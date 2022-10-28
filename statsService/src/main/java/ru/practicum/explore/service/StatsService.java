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
import java.util.*;
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
        return hit;
    }

    public Collection<ViewStats> getStats(List<String> uris, boolean unique, String start, String end) {
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

        List<Hit> list = statsRepository.findAllByUriInAndTimestampBetween(uris, startTime, endTime);

        if (unique) {
            List<ViewStats> viewStatsList = getViewStatsWithHit(list);
            List<ViewStats> resultList = viewStatsList.stream().filter(v -> v.getHits() == 1).collect(Collectors.toList());
            return resultList;
        } else {
            List<ViewStats> resultList = getViewStatsWithHit(list);
            return resultList;
        }
    }

    private ViewStats fromHitToStat(Hit hit) {
        ViewStats viewStats = new ViewStats();
        viewStats.setApp(hit.getApp());
        viewStats.setUri(hit.getUri());
        return viewStats;
    }

    private List<ViewStats> getViewStatsWithHit(List<Hit> list) {
        List<ViewStats> viewStatsList = new ArrayList<>();
        for (Hit hit : list) {
            viewStatsList.add(fromHitToStat(hit));
        }
        Set<ViewStats> viewStatsSet = new HashSet<>(viewStatsList);
        for (ViewStats stat : viewStatsSet) {
            int hits = Collections.frequency(list, stat);
            stat.setHits(hits);
        }
        return new ArrayList<>(viewStatsSet);
    }
}
