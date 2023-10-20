package org.example.housing_tracker.controllers;

import org.example.housing_tracker.domain.AppUserService;
import org.example.housing_tracker.domain.ListingsService;
import org.example.housing_tracker.domain.Result;
import org.example.housing_tracker.models.AppUser;
import org.example.housing_tracker.models.Listing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/my-homes")
public class ListingController {

    private final ListingsService listingsService;
    private final AppUserService appUserService;

    public ListingController(ListingsService listingsService, AppUserService appUserService) {
        this.listingsService = listingsService;
        this.appUserService = appUserService;
    }

    @GetMapping
    public ResponseEntity<List<Listing>> findAll() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = appUserService.loadUserByUsername(username);

        List<Listing> allListings = listingsService.findAll(user);

        if (allListings == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(allListings);
    }

    @GetMapping("/{listingId}")
    public ResponseEntity<Listing> findById(@PathVariable int listingId) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = appUserService.loadUserByUsername(username);
        
        Listing listing = listingsService.findByListingId(listingId, user.getAppUserId());

        if (listing == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(listing);
    }

    @PostMapping
    public ResponseEntity<Object> addListing(@RequestBody Listing listing) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //find user ID by user name
        AppUser user = appUserService.loadUserByUsername(username);

        Result<Listing> result = listingsService.addListing(listing, user);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{listingId}")
    public ResponseEntity<Object> update(@PathVariable int listingId, @RequestBody Listing listing) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = appUserService.loadUserByUsername(username);

        if (listingId != listing.getListingId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Listing> result = listingsService.updateListing(listing, user);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{listingId}")
    public ResponseEntity<Void> deleteById(@PathVariable int listingId) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = appUserService.loadUserByUsername(username);

        if (listingsService.deleteListing(listingId, user.getAppUserId())) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
