package org.example.housing_tracker.data;

import org.example.housing_tracker.models.Listing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ListingsJdbcTemplateRepositoryTest {
    @Autowired
    private ListingsJdbcTemplateRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Listing testListing = new Listing(2,"test@link.com",2,1500,1,1,4,true,"In-Unit","Paid residential",true);

    private Listing newListing = new Listing();

    static boolean hasSetup = false;

    @BeforeEach
    void setup() {
        if (!hasSetup) {
            hasSetup = true;
            jdbcTemplate.update("call set_known_good_state();");
        }
    }

    @Test
    void shouldFindListingById () {
        Listing foundListing = repository.findListingById(2);

        assertEquals(foundListing,testListing);
        assertEquals(foundListing.getLink(),testListing.getLink());
    }

    @Test
    void shouldNotFindNonexistentListing () {
        Listing nonexistentListing = repository.findListingById(3000);

        assertNull(nonexistentListing);
    }

    @Test
    void shouldFindListingByLink () {
        Listing foundListing = repository.findListingByLink(testListing.getLink());

        assertEquals(foundListing,testListing);
        assertEquals(foundListing.getListingId(), testListing.getListingId());
    }

    @Test
    void shouldFindAll () {
        List<Listing> allListings = repository.findAll();

        assertNotNull(allListings);
        assertEquals(allListings,2);
    }

    @Test
    void shouldCreateNewListing () {
        newListing.setListingId(3);
        newListing.setLink("create@testlink.com");
        newListing.setLocationId(1);
        newListing.setCost(1200);
        newListing.setNumBaths(2);
        newListing.setNumBeds(2);
        newListing.setAppUserId(4);
        newListing.setPetFriendly(false);
        newListing.setLaundryAvailability("Shared");
        newListing.setParking("Garage");
        newListing.setGym(false);

        Listing createdListing = repository.createListing(newListing);

        assertNotNull(createdListing);
        assertEquals(createdListing,newListing);
        assertEquals(newListing.getListingId(),createdListing.getListingId());
    }

    @Test
    void shouldNotCreateNullOrBlankListing () {
        Listing blankListing = repository.createListing(newListing);

        Listing nullListing = new Listing(0,null,0,0,0,0,0,false,null,null,false);

        assertNull(blankListing);
        assertNull(repository.createListing(nullListing));
    }

    @Test
    void shouldUpdateListing () {
        testListing.setLocationId(1);
        testListing.setLink("updated@testlink.com");
        testListing.setCost(1300);
        testListing.setNumBeds(2);
        testListing.setNumBaths(1);

        assertTrue(repository.updateListing(testListing));
        assertEquals(testListing, repository.findListingById(2));
    }

    @Test
    void shouldNotUpdateNonexistentListing () {
        Listing nonexistentListing = new Listing();
        nonexistentListing.setListingId(3000);

        assertFalse(repository.updateListing(nonexistentListing));
    }

    @Test
    void shouldDeleteListingById() {
        assertTrue(repository.deleteListingById(2));
    }

}