package org.example.housing_tracker.controllers;

import org.example.housing_tracker.domain.AppUserService;
import org.example.housing_tracker.domain.ListingsService;
import org.example.housing_tracker.domain.LocationService;
import org.example.housing_tracker.domain.Result;
import org.example.housing_tracker.models.AppUser;
import org.example.housing_tracker.models.Listing;
import org.example.housing_tracker.models.Location;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/my-locations")
public class LocationController {

    private final LocationService locationService;
    private AppUserService appUserService;
    private final String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    private final AppUser user = appUserService.loadUserByUsername(username);

    public LocationController(LocationService locationService, AppUserService appUserService) {
        this.locationService = locationService;
        this.appUserService = appUserService;
    }

    @GetMapping
    public ResponseEntity<List<Location>> findAll() {

        List<Location> allLocations = locationService.findAll(user);

        if (allLocations == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(allLocations);
    }

    @GetMapping("/{zipcode}")
    public ResponseEntity<Location> findById(@PathVariable int zipcode) {
        Location location = locationService.findLocationByZipcode(zipcode, user.getAppUserId());
        if (location == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(location);
    }

    @PostMapping
    public ResponseEntity<Object> addLocation(@RequestBody Location location) {
        Result<Location> result = locationService.findOrAddLocation(location, user);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{zipcode}")
    public ResponseEntity<Void> deleteById(@PathVariable int zipcode) {
        if (locationService.deleteLocation(zipcode, user.getAppUserId())) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
