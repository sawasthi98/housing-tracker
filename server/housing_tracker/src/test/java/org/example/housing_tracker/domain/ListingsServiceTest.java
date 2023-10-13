package org.example.housing_tracker.domain;

import org.example.housing_tracker.data.ListingsJdbcTemplateRepository;
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
    ListingsJdbcTemplateRepository repository;

    @Test
    void shouldFindAll () {
        when(repository.findAll()).thenReturn(List.of(
                new Listing(1,"testLink1@test.com",3,1200,2,2,1,false,"In-Unit","Street parking",false),
                new Listing(2,"testLink2@test.com",1,200,0,1,1,false,"Shared","Garage",false)
        ));

        List<Listing> all = service.findAll();

        assertEquals(2,all.size());
    }

    @Test
    void shouldFindListingByListingId () {
        when(repository.findAll()).thenReturn(List.of(
                new Listing(1,"testLink1@test.com",3,1200,2,2,1,false,"In-Unit","Street parking",false)
        ));

        Listing listing = service.findByListingId(1);

        assertTrue(listing.getLink().equals("testLink1@test.com"));
        assertEquals(listing.getListingId(),1);
    }

    @Test
    void shouldFindListingByLink () {
        when(repository.findAll()).thenReturn(List.of(
                new Listing(1,"testLink1@test.com",3,1200,2,2,1,false,"In-Unit","Street parking",false)
        ));

        Listing listing = service.findListingByLink("testLink1@test.com");

        assertTrue(listing.getLink().equals("testLink1@test.com"));
    }

    @Test
    void shouldNotFindIfSearchIsNullOrBlank () {
        Listing nullLinkListing = service.findListingByLink(null);
        Listing nullIdListing = service.findListingById(null);

        assertNull(nullIdListing);
        assertNull(nullLinkListing);

        Listing emptyLinkListing = service.findListingByLink("");
        assertNull(emptyLinkListing);

        Listing whiteSpaceListing = service.findListingByLink("    ");
        assertNull(whiteSpaceListing);
    }

    @Test
    void shouldNotCreateListingIfNullOrBlankFields () {
        Listing listing = null;
        Listing blankListing = new Listing();
        blankListing.setListingId(2);

        Result result = service.addListing(listing);
        Result blankResult = service.addListing(blankListing);

        assertFalse(result.isSuccess());
        assertFalse(result.isSuccess());
        assertEquals(1, result.getErrorMessages().size());
        assertEquals(1,blankResult.getErrorMessages().size());
        assertTrue(result.getErrorMessages().contains("Listing link cannot be null or blank"));
        assertTrue(blankResult.getErrorMessages().contains("Listing link cannot be null or blank"));
    }

    @Test
    void shouldCreateNewListing () {
        Listing listing = new Listing();

        listing.setListingId(1);
        listing.setLink("newLink@test.com");
        listing.setAppUserId(2);

        when(repository.createListing(listing)).thenReturn(listing);

        Result result = service.addListing(listing);

        assertTrue(result.isSuccess());
        assertNotNull(result.getErrorMessages());
    }

    @Test
    void shouldNotCreateDuplicateListing () {
        Listing listing1 = new Listing();

        listing1.setListingId(1);
        listing1.setLink("newLink@test.com");
        listing1.setAppUserId(2);

        when(repository.createListing(listing1)).thenReturn(listing1);

        Result result = service.addListing(listing1);

        assertTrue(result.isSuccess());

        Listing listing = new Listing();
        listing.setListingId(1);
        listing.setLink("newLink@test.com");
        listing.setAppUserId(2);

        when(repository.findListingByLink(listing.getLink())).thenReturn(listing);

        Result result2 = service.addListing(listing);

        assertFalse(result2.isSuccess());
    }

    @Test
    void shouldUpdateListing () {
        Listing existingListing = new Listing();

        existingListing.setListingId(1);
        existingListing.setLink("newLink@test.com");
        existingListing.setAppUserId(2);
        Listing updatedListing = new Listing();
        updatedListing.setLink("differentLink@test.com");

        when(repository.findListingByLink("newLink@test.com")).thenReturn(existingListing);
        when(repository.updateListing(updatedListing)).thenReturn(true);

        Result result = service.updateListing(updatedListing);

        assertTrue(result.isSuccess());
    }

    @Test
    void shouldDeleteListingById () {
        Listing listing = new Listing();
        listing.setListingId(1);
        listing.setLink("toDelete@test.com");
        listing.setAppUserId(2);

        when(repository.deleteListingById(1)).thenReturn(true);

        Result result = service.deleteListing(listing);

        assertTrue(result.isSuccess());
    }
}