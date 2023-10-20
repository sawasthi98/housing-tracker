package org.example.housing_tracker.domain;

import org.example.housing_tracker.data.AppUserRepository;
import org.example.housing_tracker.data.ListingsRepository;
import org.example.housing_tracker.models.AppUser;
import org.example.housing_tracker.models.Listing;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListingsService {

    ListingsRepository listingsRepository;
    AppUserRepository appUserRepository;

    public ListingsService(ListingsRepository listingsRepository, AppUserRepository appUserRepository) {
        this.listingsRepository = listingsRepository;
        this.appUserRepository = appUserRepository;
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

    public Result<Listing> findOrAddListing (Listing listing, int appUserId) {
        AppUser user = appUserRepository.findUserByAppUserId(appUserId);

        Result<Listing> result = validate(listing, user);

        if (!result.isSuccess()) {
            return result;
        }

        if (listing.getListingId() != 0) {
            result.addErrorMessage("listingId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        if (listingsRepository.createListing(listing) == null) {
            result.addErrorMessage("Unable to add new listing",ResultType.INVALID);
            return result;
        }

        try {
            List<Listing> listings = listingsRepository.findByAppUserIdAndItemId(appUserId, item.getItemId());
        }
        catch (EmptyResultDataAccessException a){//app user doesnt have this in their shelf
            ItemShelf itemShelf=itemShelfRepository.addItemToShelf(item.getItemId(),appUserId);

            if(itemShelf == null) {
                ItemShelf addedItem = itemShelfRepository.addItemToShelf(item.getItemId(), appUser.getAppUserId());
                result.setPayload(addedItem);
            }
        }

        return result;

//        add location if location by zipcode does not exist previously - locationrepo add


        listing = listingsRepository.createListing(listing);
        result.setPayload(listing);
        return result;
    }

    public Result<Listing> updateListing (Listing listing) {
        Result<Listing> result = validate(listing);

        if (listing.getListingId() <= 0) {
            result.addErrorMessage("listingId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!listingsRepository.updateListing(listing)) {
            String msg = String.format("listingId: %s, not found", listing.getListingId());
            result.addErrorMessage(msg, ResultType.NOT_FOUND);
        }

        if (!listingsRepository.updateListing(listing)) {
            result.addErrorMessage("Unable to update listing", ResultType.INVALID);
            return result;
        }

        return result;
    }

    public boolean deleteListing (int listingId){
        return listingsRepository.deleteListingById(listingId);
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

        if (result.isSuccess()) {
            List<Listing> all = listingsRepository.findAll();

            for (Listing l : all) {
                if (l.equals(listing)) {
                    result.addErrorMessage("Listing already exists for this user", ResultType.INVALID);
                }
            }
        }


        return result;
    }


}
