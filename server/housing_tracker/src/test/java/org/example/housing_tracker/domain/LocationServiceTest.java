package org.example.housing_tracker.domain;

import org.example.housing_tracker.data.LocationRepository;
import org.example.housing_tracker.models.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class LocationServiceTest {

    @Autowired
    LocationService service;

    @MockBean
    LocationRepository repository;

    @Test
    void shouldFindAll() {
        when(repository.findAll()).thenReturn(List.of(
                new Location(1,"Charlotte", "NC", 28278),
                new Location(2, "Charlotte", "NC", 28217)
        ));

        List<Location> all = service.findAll();

        assertEquals(2,all.size());
    }

    @Test
    void shouldFindLocationByZipcode () {
        Location existingLocation = new Location(1,"Charlotte", "NC", 28278);

        when(repository.findLocationByZipcode(28278)).thenReturn(existingLocation);

        Location foundLocation = service.findLocationByZipcode(28278);

        assertTrue(existingLocation.equals(foundLocation));
        assertEquals(existingLocation.getZipCode(),foundLocation.getZipCode());
    }

    @Test
    void shouldAddLocation () {
        Location location = new Location();

        location.setCity("Philadelphia");
        location.setState("PA");
        location.setZipCode(19104);

        when(repository.addLocation(location)).thenReturn(location);

        Result<Location> result = service.findOrAddLocation(location);

        assertTrue(result.isSuccess());
        assertNotNull(result.getErrorMessages());
    }

    @Test
    void shouldDeleteLocation () {
        Location location = new Location();

        location.setCity("Philadelphia");
        location.setState("PA");
        location.setZipCode(19104);

        when(repository.deleteLocationByZipcode(19104)).thenReturn(true);

        boolean deletedResult = service.deleteLocation(19104);

        assertTrue(deletedResult);
    }


}