package org.example.housing_tracker.domain;

import org.example.housing_tracker.data.ListingsJdbcTemplateRepository;
import org.example.housing_tracker.data.ListingsRepository;
import org.example.housing_tracker.models.Listing;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ListingsServiceTest {

    @Autowired
    ListingsService service;

    @MockBean
    ListingsRepository repository;

    @Test
    void shouldFindTestListingById () {
        Listing mansion = new Listing(1,"test@test.com",3,1200,2,2,4,true,"In-Unit","Street",true);

        when(repository.findListingById(1)).thenReturn(mansion);

        Listing foundListing = service.findListingById(1);

        assertNotNull(foundListing);
        assertEquals(foundListing.getListingId(),mansion.getListingId());
    }

    @Test
    void shouldFindTestListingByLink () {
        Listing mansion = new Listing(1,"test@test.com",3,1200,2,2,4,true,"In-Unit","Street",true);

        when(repository.findListingByLink("test@test.com")).thenReturn(mansion);

        Listing foundListing = service.findListingByLink("test@test.com");

        assertNotNull(foundListing);
        assertEquals(foundListing.getLink(),mansion.getLink());
    }

    @Test
    void shouldNotFindIfLinkIsNullOrBlank () {
        Listing nullListing = service.findListingByLink(null);
        assertNull(nullListing);

        Listing blankListing = service.findListingByLink("");
        assertNull(blankListing);

        Listing whiteSpaceListing = service.findListingByLink("    ");
        assertNull(whiteSpaceListing);
    }

    @Test
    void shouldAddNewListing () {
        Listing listing = new Listing(4,"add@newlisting.com",2,1500,3,1,1,false,"Shared","Garage",true);

        when(repository.addListing(listing)).thenReturn(listing);

        Result result = service.addListing(listing);

        assertTrue(result.isSuccess());
        assertNotNull(result.getErrorMessages());
    }

    @Test
    void shouldNotAddDuplicateListing () {
        Listing listing = new Listing();
        listing.setLink("test@test.com");
        listing.setNumBeds(0);
        listing.setNumBaths(1);
        listing.setLocationId(2);
        listing.setCost(1300);
        listing.setAppUserId(2);

        when(repository.addListing(listing)).thenReturn(listing);

        Result result = service.addListing(listing);

        assertTrue(result.isSuccess());

        Listing duplicate = new Listing();
        duplicate.setLink("testing@test.com");
        duplicate.setNumBeds(0);
        duplicate.setNumBaths(1);
        duplicate.setLocationId(2);
        duplicate.setCost(1300);
        listing.setAppUserId(2);

        when(repository.findListingById(duplicate.getListingId())).thenReturn(duplicate);

        Result result2 = service.addListing(duplicate);

        assertFalse(result2.isSuccess());
    }

    

}