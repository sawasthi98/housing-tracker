package org.example.housing_tracker.data.mappers;

import org.example.housing_tracker.models.Listing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ListingMapper implements RowMapper<Listing> {

    private JdbcTemplate jdbcTemplate;

    public ListingMapper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Listing mapRow(ResultSet rs, int rowNum) throws SQLException {
        Listing listing = new Listing();
        listing.setListingId(rs.getInt("listing_id"));
        listing.setLocationId(rs.getInt("location_id"));
        listing.setLink(rs.getString("link"));
        listing.setCost(rs.getInt("cost"));
        listing.setNumBeds(rs.getInt("num_beds"));
        listing.setNumBaths(rs.getInt("num_baths"));
        listing.setAppUserId(rs.getInt("app_user_id"));
        listing.setPetFriendly(rs.getBoolean("pet_friendly"));
        listing.setLaundryAvailability(rs.getString("laundry"));
        listing.setParking(rs.getString("parking"));
        listing.setGym(rs.getBoolean("gym"));

        return listing;
    }



}
