package org.example.housing_tracker.data;

import org.example.housing_tracker.models.Listing;

import java.util.List;

public class ListingsJdbcTemplateRepository implements ListingsRepository {
    @Override
    public Listing findListingById(int listingId) {

        return null;
    }

    @Override
    public Listing findListingByLink(String link) {
        return null;
    }

    @Override
    public List<Listing> findAll() {
        return null;
    }

    @Override
    public Listing createListing(Listing listing) {
        return null;
    }

    @Override
    public boolean updateListing(Listing listing) {
        return false;
    }

    @Override
    public boolean deleteListingById(int listingId) {
        return false;
    }
}
