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
        Listing existingListing = new Listing(1,"link@firstlink.com",1,2000,2,1,1,false,"In-Unit","Street",true);

        when(repository.findListingById(1)).thenReturn(existingListing);

        Listing listing = service.findByListingId(1);

        assertTrue(listing.getLink().equals("link@firstlink.com"));
        assertEquals(listing.getListingId(),1);
    }

    @Test
    void shouldFindListingByLink () {
        Listing existingListing = new Listing(1,"link@firstlink.com",1,2000,2,1,1,false,"In-Unit","Street",true);

        when(repository.findListingByLink("link@firstlink.com")).thenReturn(existingListing);

        Listing listing = service.findListingByLink("link@firstlink.com");

        assertTrue(listing.getLink().equals("link@firstlink.com"));
    }

    @Test
    void shouldNotFindIfSearchIsNullOrBlank () {
        Listing nullLinkListing = service.findListingByLink(null);

        assertNull(nullLinkListing);

        Listing emptyLinkListing = service.findListingByLink("");
        assertNull(emptyLinkListing);

        Listing whiteSpaceListing = service.findListingByLink("    ");
        assertNull(whiteSpaceListing);
    }

    @Test
    void shouldNotCreateListingIfNullOrBlankFields () {
        Listing listing = new Listing(95,"TEST",1,2000,2,1,1,false,"In-Unit","Street",true);

        Result<Listing> actual = service.addListing(listing);
        assertEquals(ResultType.INVALID, actual.getResultType());

        listing.setListingId(0);
        listing.setLink(null);

        actual = service.addListing(listing);
        assertEquals(ResultType.INVALID, actual.getResultType());

        listing.setParking("TEST");
        listing.setLaundryAvailability("   ");
        actual = service.addListing(listing);
        assertEquals(ResultType.INVALID, actual.getResultType());

        assertFalse(actual.isSuccess());
        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getErrorMessages().size());
    }

    @Test
    void shouldCreateNewListing () {
        Listing listing = new Listing();

        listing.setLink("newLink@test.com");
        listing.setAppUserId(2);

        when(repository.createListing(listing)).thenReturn(listing);

        Result<Listing> result = service.addListing(listing);

        assertTrue(result.isSuccess());
        assertNotNull(result.getErrorMessages());
    }

    @Test
    void shouldNotCreateDuplicateListing () {
        Listing duplicateListing = new Listing();
        duplicateListing.setLink("link@firstlink.com");

//        when(repository.createListing(duplicateListing)).thenReturn(null);

        Result<Listing> result = service.addListing(duplicateListing);

        assertFalse(result.isSuccess());
//        assertEquals(result.getErrorMessages(),"Listing already exists for this user");
    }

    @Test
    void shouldUpdateListing () {
        Listing listing = new Listing(1,"link@firstlink.com",1,2000,2,1,1,false,"In-Unit","Street",true);

        when(repository.updateListing(listing)).thenReturn(true);

        Result<Listing> result = service.updateListing(listing);

        assertTrue(result.isSuccess());
        assertEquals(ResultType.SUCCESS, result.getResultType());
    }

    @Test
    void shouldDeleteListingById () {
        Listing listing = new Listing();
        listing.setListingId(1);
        listing.setLink("toDelete@test.com");
        listing.setAppUserId(2);

        when(repository.deleteListingById(1)).thenReturn(true);

        boolean deletedResult = service.deleteListing(1);

        assertTrue(deletedResult);
    }
}