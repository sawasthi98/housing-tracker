package org.example.housing_tracker.domain;

import org.example.housing_tracker.App;
import org.example.housing_tracker.data.ListingsRepository;
import org.example.housing_tracker.models.AppUser;
import org.example.housing_tracker.models.Listing;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListingsService {

    ListingsRepository repository;

    public ListingsService(ListingsRepository repository) {
        this.repository = repository;
    }

    public List<Listing> findAll (AppUser user) {
        return repository.findAll(user.getAppUserId());
    }

    public Listing findListingByLink (String link) {
        return repository.findListingByLink(link);
    }

    public Listing findByListingId (int listingId, int appUserId) {
        return repository.findListingById(listingId, appUserId);
    }

    public Result<Listing> addListing (Listing listing, AppUser user) {
        Result<Listing> result = validate(listing, user);

        if (!result.isSuccess()) {
            return result;
        }

        if (listing.getListingId() != 0) {
            result.addErrorMessage("listingId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        if (repository.createListing(listing) == null) {
            result.addErrorMessage("Unable to add new listing",ResultType.INVALID);
            return result;
        }

//        add location if location by zipcode does not exist previously - locationrepo add


        listing = repository.createListing(listing);
        result.setPayload(listing);
        return result;
    }

    public Result<Listing> updateListing (Listing listing, AppUser user) {
        Result<Listing> result = validate(listing, user);

        if (listing.getListingId() <= 0) {
            result.addErrorMessage("listingId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!repository.updateListing(listing)) {
            String msg = String.format("listingId: %s, not found", listing.getListingId());
            result.addErrorMessage(msg, ResultType.NOT_FOUND);
        }

        if (!repository.updateListing(listing)) {
            result.addErrorMessage("Unable to update listing", ResultType.INVALID);
            return result;
        }

        return result;
    }

    public boolean deleteListing (int listingId, int appUserId){
        return repository.deleteListingById(listingId, appUserId);
    }

    private Result<Listing> validate (Listing listing, AppUser user) {
        Result<Listing> result = new Result<>();

        if (listing == null) {
            result.addErrorMessage("Listing cannot be null or blank", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(listing.getLink())) {
            result.addErrorMessage("Listing link is required", ResultType.INVALID);
        }

        if (result.isSuccess()) {
            List<Listing> all = repository.findAll(user.getAppUserId());

            for (Listing l : all) {
                if (l.equals(listing)) {
                    result.addErrorMessage("Listing already exists for this user", ResultType.INVALID);
                }
            }
        }


        return result;
    }


}
