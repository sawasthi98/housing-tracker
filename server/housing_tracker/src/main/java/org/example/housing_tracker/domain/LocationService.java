package org.example.housing_tracker.domain;

import org.example.housing_tracker.data.LocationRepository;
import org.example.housing_tracker.models.Location;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    LocationRepository repository;

    public LocationService(LocationRepository repository) {
        this.repository = repository;
    }

    public List<Location> findAll() {
        return repository.findAll();
    }

    public Location findLocationByZipcode (int zipcode) {
        return repository.findLocationByZipcode(zipcode);
    }

    public Result<Location> addLocation (Location location) {
        Result result = validate(location);

        if (!result.isSuccess()) {
            return result;
        }

        if (location.getLocationId() != 0) {
            result.addErrorMessage("locationId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        if (repository.addLocation(location) == null) {
            result.addErrorMessage("Unable to add new location.",ResultType.INVALID);
            return result;
        }

        location = repository.addLocation(location);
        result.setPayload(location);
        return result;
    }

    public boolean deleteLocation (int zipcode) {
        return repository.deleteLocationByZipcode(zipcode);
    }

    private Result<Location> validate (Location location) {
        Result<Location> result = new Result<>();

        if (location == null) {
            result.addErrorMessage("Location cannot be null or blank", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(location.getState())) {
            result.addErrorMessage("Location state is required and cannot be left blank", ResultType.INVALID);
        }

        if (Validations.isNullOrBlank(location.getCity())) {
            result.addErrorMessage("Location city is required and cannot be left blank", ResultType.INVALID);
        }

//        List<Location> all = repository.findAll();
//
//        for (Location l : all) {
//            if (l != location) {
//                result.addErrorMessage("Location could not be found",ResultType.NOT_FOUND);
//            }
//        }

        return result;
    }


}
