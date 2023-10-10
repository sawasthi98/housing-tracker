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
        assertTrue(listing.get);
    }

    @Test
    void shouldFindListingByLink () {

    }

    @Test
    void shouldNotFindIfSearchIsNullOrBlank () {

    }

    @Test
    void shouldNotCreateListingIfNullOrBlankFields () {

    }

    @Test
    void shouldCreateNewListing () {

    }

    @Test
    void shouldNotCreateDuplicateListing () {

    }

    @Test
    void shouldUpdateListing () {

    }

    @Test
    void shouldNotUpdateNonexistentListing () {

    }

    @Test
    void shouldDeleteListingById () {

    }
}