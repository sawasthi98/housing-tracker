package org.example.housing_tracker.data;

import org.example.housing_tracker.models.AppUser;
import org.example.housing_tracker.models.Location;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
public interface LocationRepository {
    public List<Location> findAll(int appUserId);
    public Location findLocationByZipcode(int zipcode, int appUserId);
    public Location addLocation(Location location);
    public boolean deleteLocationByZipcode(int zipcode, int appUserId);
}
