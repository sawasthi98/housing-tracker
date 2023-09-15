package org.example.housing_tracker.data;

import org.example.housing_tracker.models.Listing;

import java.util.List;

public interface ListingsRepository {

//    find bys
    public Listing findListingById (int listingId);
    public Listing findListingByLink (String link);
    public List<Listing> findAll ();

//    create
    public Listing createListing (Listing listing);
//    update
    public boolean updateListing (Listing listing);
//    delete
    public boolean deleteListingById (int listingId);


}
