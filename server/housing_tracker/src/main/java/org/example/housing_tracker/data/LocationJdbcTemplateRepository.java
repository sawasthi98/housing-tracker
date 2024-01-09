package org.example.housing_tracker.data;

import org.example.housing_tracker.data.mappers.ListingMapper;
import org.example.housing_tracker.data.mappers.LocationMapper;
import org.example.housing_tracker.models.Location;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class LocationJdbcTemplateRepository implements LocationRepository {
    private JdbcTemplate jdbcTemplate;

    public LocationJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Location> findAll(int appUserId) {
        final String sql = "select location_id, city, state, zipcode, app_user_id " +
                "from location " +
                "where app_user_id = ?;";

//        "select l.location_id, l.city, l.state, l.zipcode, au.app_user_id " +
//                "from location l inner join app_user au on l.app_user_id = au.app_user_id " +
//                "where l.app_user_id = ?;";

        return jdbcTemplate.query(sql, new LocationMapper(jdbcTemplate),appUserId);
    }

    @Override
    public Location findLocationByZipcode (int zipcode, int appUserId) {
        final String sql = "select location_id, city, state, zipcode, app_user_id " +
                "from location " +
                "where zipcode = ? " +
                "and app_user_id = ?;";

//                "select l.location_id, l.city, l.state, l.zipcode, au.app_user_id " +
//                "from location l inner join app_user au on l.app_user_id = au.app_user_id " +
//                "where l.zipcode = ? " +
//                "and l.app_user_id = ?;";

        return jdbcTemplate.query(sql, new LocationMapper(jdbcTemplate), zipcode, appUserId).stream()
                .findAny().orElse(null);
    }

    @Override
    public Location addLocation (Location location) {
        final String sql = "insert into location (city, state, zipcode, app_user_id) " +
                "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, location.getCity());
            statement.setString(2, location.getState());
            statement.setInt(3, location.getZipCode());
            statement.setInt(4,location.getAppUserId());

            return statement;
        }, keyHolder);

        if (rowsAffected == 0) {
            return null;
        }

        location.setLocationId(keyHolder.getKey().intValue());

        return location;
    }

    @Override
    public boolean deleteLocationByZipcode (int zipcode, int appUserId) {
        // allows user to delete all listings connected to that location at once
        Location location = findLocationByZipcode(zipcode, appUserId);

        jdbcTemplate.update("delete from listings where location_id = ?;", location.getLocationId());
        return jdbcTemplate.update("delete from location where location_id = ?;", location.getLocationId()) > 0;
    }

}
