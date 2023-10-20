package org.example.housing_tracker.data;

import org.example.housing_tracker.models.Listing;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
public interface ListingsRepository {

//    find bys
    public Listing findListingById (int listingId, int appUserId);
    public Listing findListingByLink (String link, int appUserId);
    public List<Listing> findAll (int appUserId);
//    create
    public Listing createListing (Listing listing);
//    update
    public boolean updateListing (Listing listing);
//    delete
    public boolean deleteListingById (int listingId, int appUserId);


}
