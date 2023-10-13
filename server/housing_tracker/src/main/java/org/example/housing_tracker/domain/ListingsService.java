package org.example.housing_tracker.domain;

import org.example.housing_tracker.data.ListingsRepository;
import org.example.housing_tracker.models.Listing;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListingsService {

    ListingsRepository repository;

    public ListingsService(ListingsRepository repository) {
        this.repository = repository;
    }

    public List<Listing> findAll () {
        return repository.findAll();
    }

    public Listing findListingByLink (String link) {
        return repository.findListingByLink(link);
    }

    public Listing findByListingId (int listingId) {
        return repository.findListingById(listingId);
    }

    public Result<Listing> addListing (Listing listing) {
        Result result = validate(listing);

        if (!result.isSuccess()) {
            return result;
        }

        if (listing.getListingId() != 0) {
            result.addErrorMessage("listingId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        if (repository.createListing(listing) == null) {
            result.addErrorMessage("Unable to add new listing.",ResultType.INVALID);
            return result;
        }

//        add location if location by zipcode does not exist previously - locationrepo add
        

        listing = repository.createListing(listing);
        result.setPayload(listing);
        return result;
    }

    public Result<Listing> updateListing (Listing listing) {
        Result result = validate(listing);

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

    public boolean deleteListing (int listingId){
        return repository.deleteListingById(listingId);
    }

    private Result<Listing> validate (Listing listing) {
        Result<Listing> result = new Result<>();

        if (listing == null) {
            result.addErrorMessage("Listing cannot be null or blank", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(listing.getLink())) {
            result.addErrorMessage("Listing link is required", ResultType.INVALID);
        }

        List<Listing> all = repository.findAll();

        for (Listing l : all) {
            if ( l != listing) {
                result.addErrorMessage("Listing could not be found", ResultType.NOT_FOUND);
            }

            if (l.equals(listing)) {
                result.addErrorMessage("Listing already exists for this user", ResultType.INVALID);
            }

            if (l.getListingId() == listing.getListingId()) {
                result.addErrorMessage("Listing already exists for this user", ResultType.INVALID);
            }
        }

        return result;
    }


}
