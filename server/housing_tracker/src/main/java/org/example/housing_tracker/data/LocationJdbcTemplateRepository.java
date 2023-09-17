package org.example.housing_tracker.data;

import org.example.housing_tracker.data.mappers.ListingMapper;
import org.example.housing_tracker.data.mappers.LocationMapper;
import org.example.housing_tracker.models.Location;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LocationJdbcTemplateRepository implements LocationRepository {
    private JdbcTemplate jdbcTemplate;

    public LocationJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Location> findAll() {
        final String sql = "select location_id, city, state, zipcode " +
                "from location;";

        return jdbcTemplate.query(sql, new LocationMapper(jdbcTemplate));
    }


//    @Override
//    public Location findByLocationId(int locationId) {
//        return null;
//    }
//
//    @Override
//    public Location findByCity(String city) {
//        return null;
//    }
//
//    @Override
//    public Location findByState(String state) {
//        return null;
//    }
//
//    @Override
//    public Location findByZipcode(int zipcode) {
//        return null;
//    }
}
