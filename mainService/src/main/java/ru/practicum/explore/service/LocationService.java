package ru.practicum.explore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explore.model.Location;
import ru.practicum.explore.storage.LocationRepository;

import java.util.Optional;

@Slf4j
@Service
public class LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location getLocation(long id) {
        Optional<Location> optional = locationRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public Location addLocation(Location location) {
        return locationRepository.save(location);
    }
}
