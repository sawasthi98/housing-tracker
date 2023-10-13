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

    public Result addListing (Listing listing) {
        Result result = new Result();

        if (listing == null || listing.getLink().isBlank()) {
            result.addErrorMessage("Listing link cannot be null or blank", ResultType.NOT_FOUND);
            return result;
        }
        for (Listing l : repository.findAll()){
            if (l.equals(listing)) {
                result.addErrorMessage("This listing already exists for this user", ResultType.INVALID);
                return result;
            }
            if (l.getListingId() == listing.getListingId()) {
                result.addErrorMessage("Listing already exists for this user", ResultType.INVALID);
                return result;
            }
        }

        if (repository.createListing(listing) == null) {
            result.addErrorMessage("Unable to add new listing.",ResultType.INVALID);
            return result;
        }

        return result;
    }

    public Result updateListing (Listing listing) {
        Result result = new Result();

        if (listing == null || listing.getLink() == null || listing.getLink().isBlank()){
            result.addErrorMessage("Link cannot be null or blank.", ResultType.NOT_FOUND);
            return result;
        }
        if (repository.findListingById(listing.getListingId()) != listing) {
            result.addErrorMessage("This is not the same review",ResultType.INVALID);
            return result;
        }
        if (!repository.updateListing(listing)) {
            result.addErrorMessage("Unable to update listing", ResultType.INVALID);
            return result;
        }
        return result;
    }

    public Result deleteListing (Listing listing){
        Result result = new Result();

//        if (listing == null || listing.getLink().isBlank()) {
//            result.addErrorMessage("Listing link cannot be null or blank.", ResultType.NOT_FOUND);
//            return result;
//        }

        if (!repository.deleteListingById(listing.getListingId())) {
            result.addErrorMessage("Unable to delete listing",ResultType.INVALID);
            return result;
        }

        return result;
    }


}
