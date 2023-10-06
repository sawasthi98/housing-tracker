package org.example.housing_tracker.data.mappers;

import org.example.housing_tracker.models.Listing;
import org.example.housing_tracker.models.Location;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationMapper implements RowMapper<Location> {
    private JdbcTemplate jdbcTemplate;

    public LocationMapper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
        Location location = new Location();
        location.setLocationId(rs.getInt("location_id"));
        location.setCity(rs.getString("city"));
        location.setState(rs.getString("state"));
        location.setZipCode(rs.getInt("zipcode"));

        return location;
    }
}
