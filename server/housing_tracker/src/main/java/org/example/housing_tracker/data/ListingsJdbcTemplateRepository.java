package org.example.housing_tracker.data;

import org.example.housing_tracker.data.mappers.ListingMapper;
import org.example.housing_tracker.models.Listing;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ListingsJdbcTemplateRepository implements ListingsRepository {
    private JdbcTemplate jdbcTemplate;

    public ListingsJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Listing findListingById(int listingId, int appUserId) throws DataAccessException {
        final String sql = "select listing_id, location_id, link, cost, num_beds, num_baths, app_user_id, pet_friendly, laundry, parking, gym " +
                "from listings " +
                "where listing_id = ? " +
                "and app_user_id = ?;";

        return jdbcTemplate.query(sql, new ListingMapper(jdbcTemplate), listingId, appUserId).stream().findFirst().orElse(null);
    }

    @Override
    public Listing findListingByLink(String link, int appUserId) throws DataAccessException {
        final String sql = "select listing_id, location_id, link, cost, num_beds, num_baths, app_user_id, pet_friendly, laundry, parking, gym " +
                "from listings " +
                "where link = ? " +
                "and app_user_id = ?;";

        List<Listing> all = jdbcTemplate.query(sql, new ListingMapper(jdbcTemplate), link, appUserId);
        return (all == null || all.size() == 0 ? null : all.get(0));
    }

    @Override
    public List<Listing> findAll(int appUserId) throws DataAccessException {
        final String sql = "select listing_id, location_id, link, cost, num_beds, num_baths, app_user_id, pet_friendly, laundry, parking, gym " +
                "from listings " +
                "where app_user_id = ? " +
                "order by location_id;";
////        default ordering by city/zipcode

        return jdbcTemplate.query(sql, new ListingMapper(jdbcTemplate), appUserId);
    }

    @Override
    public Listing createListing(Listing listing) throws DataAccessException {
        final String sql = "insert into listings (location_id, link, cost, num_beds, num_baths, app_user_id, pet_friendly, laundry, parking, gym)" +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, listing.getLocationId());
            statement.setString(2, listing.getLink());
            statement.setInt(3, listing.getCost());
            statement.setFloat(4, listing.getNumBeds());
            statement.setFloat(5, listing.getNumBaths());
            statement.setInt(6, listing.getAppUserId());
            statement.setBoolean(7, listing.isPetFriendly());
            statement.setString(8, listing.getLaundryAvailability());
            statement.setString(9, listing.getParking());
            statement.setBoolean(10, listing.hasGym());

            return statement;
        }, keyHolder);

        if (rowsAffected == 0) {
            return null;
        }

        listing.setListingId(keyHolder.getKey().intValue());

        return listing;
    }

    @Override
    public boolean updateListing(Listing listing) throws DataAccessException {
        final String sql = "update listings set " +
                "location_id = ?, " +
                "link = ?, " +
                "cost = ?, " +
                "num_beds = ?, " +
                "num_baths = ?, " +
                "app_user_id = ?, " +
                "pet_friendly = ?, " +
                "laundry = ?, " +
                "parking = ?, " +
                "gym = ? " +
                "where listing_id = ?;";

        return jdbcTemplate.update(sql,
                listing.getLocationId(),
                listing.getLink(),
                listing.getCost(),
                listing.getNumBeds(),
                listing.getNumBaths(),
                listing.getAppUserId(),
                listing.isPetFriendly(),
                listing.getLaundryAvailability(),
                listing.getParking(),
                listing.hasGym(),
                listing.getListingId())
        > 0;
    }

    @Override
    public boolean deleteListingById (int listingId, int appUserId) throws DataAccessException {
//        delete cascade - comments is dependent on listings table
        jdbcTemplate.update("delete from comments where listing_id = ?;", listingId);
        return jdbcTemplate.update("delete from listings where listing_id = ? " +
                "and app_user_id = ?;", listingId, appUserId) > 0;
    }
}
