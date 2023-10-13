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
    public List<Location> findAll() {
        final String sql = "select location_id, city, state, zipcode " +
                "from location;";

        return jdbcTemplate.query(sql, new LocationMapper(jdbcTemplate));
    }

    @Override
    public Location findLocationByZipcode (int zipcode) {
        final String sql = "select location_id, city, state, zipcode " +
                "from location " +
                "where zipcode = ?;";

        return jdbcTemplate.query(sql, new LocationMapper(jdbcTemplate), zipcode).stream()
                .findAny().orElse(null);
    }

    @Override
    public Location addLocation (Location location) {
        final String sql = "insert into location (city, state, zipcode) " +
                "values (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, location.getCity());
            statement.setString(2, location.getState());
            statement.setInt(3, location.getZipCode());

            return statement;
        }, keyHolder);

        if (rowsAffected == 0) {
            return null;
        }

        location.setLocationId(keyHolder.getKey().intValue());

        return location;
    }


}
