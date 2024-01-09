package org.example.housing_tracker.domain;

import org.example.housing_tracker.data.LocationRepository;
import org.example.housing_tracker.models.AppUser;
import org.example.housing_tracker.models.Location;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    LocationRepository repository;

    public LocationService(LocationRepository repository) {
        this.repository = repository;
    }

    public List<Location> findAll(AppUser user) {
        return repository.findAll(user.getAppUserId());
    }

    public Location findLocationByZipcode (int zipcode, int appUserId) {
        return repository.findLocationByZipcode(zipcode, appUserId);
    }

    public Result<Location> findOrAddLocation (Location location, AppUser user) {
        Result<Location> result = validate(location, user);

        if (!result.isSuccess()) {
            return result;
        }

//        if (findLocationByZipcode(location.getZipCode(), user.getAppUserId()) == null) {
            if (location.getLocationId() != 0) {
                result.addErrorMessage("locationId cannot be set for `add` operation", ResultType.INVALID);
                return result;
            }

//            if (repository.addLocation(location) == null) {
//                result.addErrorMessage("Unable to add new location.",ResultType.INVALID);
//                return result;
//            }

            location = repository.addLocation(location);
            if (location != null) {
                result.setPayload(location);
            }
            return result;
//        }

//        return result;
    }

    public boolean deleteLocation (int zipcode, int appUserId) {
        return repository.deleteLocationByZipcode(zipcode, appUserId);
    }

    private Result<Location> validate (Location location, AppUser user) {
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

        // doesn't seem necessary as there are multiple listings for one location
        if (result.isSuccess()) {
            List<Location> all = repository.findAll(user.getAppUserId());

            for (Location l : all) {
                if (l.equals(location)) {
                    result.addErrorMessage("Location already exists for this user",ResultType.INVALID);
                }
            }
        }




        return result;
    }


}
