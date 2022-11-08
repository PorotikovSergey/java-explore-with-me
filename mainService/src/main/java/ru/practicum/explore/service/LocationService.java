package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.model.Location;
import ru.practicum.explore.storage.LocationRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public Location getLocation(long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Такой локации по данному id нет в базе данных"));
        log.info("Из бд получено следующее местоположение {}", location);
        return location;
    }

    public Location addLocation(Location location) {
        Location addedLocation = locationRepository.save(location);
        log.info("Локация {} добавлена в бд", location);
        return addedLocation;
    }
}
