package org.example.housing_tracker.data;

import org.example.housing_tracker.models.Location;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
public interface LocationRepository {

    public List<Location> findAll ();
//    public Location findByLocationId (int locationId);
//    public Location findByCity (String city);
//    public Location findByState (String state);
//    public Location findByZipcode (int zipcode);

}
