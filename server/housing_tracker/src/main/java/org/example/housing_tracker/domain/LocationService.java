package org.example.housing_tracker.domain;

import org.example.housing_tracker.data.LocationRepository;
import org.example.housing_tracker.models.Location;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    private LocationRepository repository;

    public LocationService(LocationRepository repository) {
        this.repository = repository;
    }

    public Location findOrCreate (Location location) {
        Location foundLocation = repository.findLocationByZipcode(location.getZipCode());

        if (foundLocation != null) {
            return foundLocation;
        }

        return repository.addLocation(location);
    }


}
