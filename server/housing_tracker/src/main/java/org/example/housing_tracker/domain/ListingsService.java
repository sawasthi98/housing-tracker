package org.example.housing_tracker.domain;

import org.example.housing_tracker.data.CommentRepository;
import org.example.housing_tracker.data.ListingsRepository;
import org.example.housing_tracker.models.Listing;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListingsService {

    private ListingsRepository repository;

    public ListingsService(ListingsRepository repository) {
        this.repository = repository;
    }

    public List<Listing> findAll () {
        return repository.findAll();
    }

    public Listing findListingById (int listingId) {
        if (listingId > 0) {
            return repository.findListingById(listingId);
        }

        return null;
    }

    public Listing findListingByLink (String link) {
        if (link != null || !link.isBlank()) {
            return repository.findListingByLink(link);
        }

        return null;
    }

    public Result<Listing> addListing (Listing listing) {
        Result result = validate(listing);

        if (!result.isSuccess()) {
            return result;
        }

        if (listing.getListingId() != 0) {
            result.addErrorMessage("Listing ID cannot be set for the `add` operation.",ResultType.INVALID);
            return result;
        }

        for (Listing existing : repository.findAll() ) {
            if (existing.equals(listing)) {
                result.addErrorMessage("Listing already exists in the system", ResultType.INVALID);
                return result;
            }
        }

        listing = repository.addListing(listing);
        result.setPayload(listing);
        return result;
    }

    public Result<Listing> update (Listing listing) {
        Result<Listing> result = validate(listing);
        if (!result.isSuccess()) {
            return result;
        }

        if (listing.getListingId() <= 0) {
            result.addErrorMessage("Listing ID must be set for the `update` operation.",ResultType.INVALID);
            return result;
        }

        if (!repository.updateListing(listing)) {
            String msg = String.format("listingID %s not found", listing.getListingId());
            result.addErrorMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteListingById (int listingId) {
        return repository.deleteListingById(listingId);
    }

    private Result<Listing> validate (Listing listing) {
        Result result = new Result();

        if (listing == null) {
            result.addErrorMessage("Listing cannot be null.", ResultType.INVALID);
            return result;
        }

        if (listing.getLink().isBlank()) {
            result.addErrorMessage("Listing link cannot be left blank", ResultType.INVALID);
        }

        if (listing.getLink() == null) {
            result.addErrorMessage("Listing could not be found. Please check the link again",ResultType.INVALID);
        }

        if (listing.getNumBaths() < 1) {
            result.addErrorMessage("Number of baths must be a number larger than 0.",
                    ResultType.INVALID);
        }

        if (listing.getLocationId() < 1) {
            result.addErrorMessage("Location ID must be greater than 0",
                    ResultType.INVALID);
        }

        if (listing.getCost() <= 0) {
            result.addErrorMessage("Cost must be a number larger than 0", ResultType.INVALID);
        }

        if (listing.getAppUserId() == 0) {
            result.addErrorMessage("App user cannot be null or 0", ResultType.INVALID);
        }

        // need to make sure it's not a duplicate
        if (result.isSuccess()) {
            List<Listing> existingListings = repository.findAll();

            for (Listing existingListing : existingListings) {
                if (existingListing.getListingId() == listing.getListingId() &&
                        existingListing.getLocationId() == listing.getLocationId() &&
                        existingListing.getLink().equals(listing.getLink()) &&
                        existingListing.getCost() == listing.getCost() &&
                        existingListing.getNumBeds() == listing.getNumBeds() &&
                        existingListing.getNumBaths() == listing.getNumBaths() &&
                        existingListing.getAppUserId() == listing.getAppUserId() &&
                        existingListing.isPetFriendly() == listing.isPetFriendly() &&
                        existingListing.hasGym() == listing.hasGym() &&
                        existingListing.getParking().equals(listing.getParking()) &&
                        existingListing.getLaundryAvailability().equals(listing.getLink())) {
                    result.addErrorMessage("Listing already exists. Make sure all fields are unique." , ResultType.INVALID);
                }
            }

        }

        return result;
    }
}
