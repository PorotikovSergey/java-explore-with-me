package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore.model.Hit;
import ru.practicum.explore.model.ViewStats;
import ru.practicum.explore.storage.StatsRepository;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;

    public Hit postHit(Hit hit) {
        statsRepository.save(hit);
        return hit;
    }

    public Collection<ViewStats> getStats(List<String> uris, boolean unique, String start, String end) {
        if (unique) {
            List<Hit> listUnique = statsRepository.findDistinctByUriInAndTimestampBetween(uris, start, end);
            return getViewStatsWithHit(listUnique);
        } else {
            List<Hit> list = statsRepository.findAllByUriInAndTimestampBetween(uris, start, end);
            return getViewStatsWithHit(list);
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
            int hits = list.size();
            stat.setHits(hits);
        }
        return new ArrayList<>(viewStatsSet);
    }
}
