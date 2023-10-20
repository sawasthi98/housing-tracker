package org.example.housing_tracker.controllers;

import org.example.housing_tracker.domain.AppUserService;
import org.example.housing_tracker.domain.ListingsService;
import org.example.housing_tracker.domain.Result;
import org.example.housing_tracker.models.AppUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.example.housing_tracker.models.Listing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<Listing> findAll() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = appUserService.loadUserByUsername(username);

        // service find by app user id
        List<Listing> allListings = listingsService.findByAppUserId(user.getAppUserId());

        if (allListings != null) {
            return new ResponseEntity<>(allListings, HttpStatus.OK);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/{listingId}")
    public ResponseEntity<Listing> findById(@PathVariable int listingId) {
        Listing listing = service.findByListingId(listingId);
        if (listing == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(listing);
    }

    @PostMapping
    public ResponseEntity<Object> addListing(@RequestBody Listing listing) {
        Result<Listing> result = service.addListing(listing);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{listingId}")
    public ResponseEntity<Object> updateListing(@PathVariable int listingId, @RequestBody Listing listing) {
        if (listingId != listing.getListingId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Listing> result = service.updateListing(listing);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{listingId}")
    public ResponseEntity<Void> deleteById(@PathVariable int listingId) {
        if (service.deleteListing(listingId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
