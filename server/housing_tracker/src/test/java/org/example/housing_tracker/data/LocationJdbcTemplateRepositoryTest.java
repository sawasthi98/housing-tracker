package org.example.housing_tracker.data;

import org.example.housing_tracker.models.Listing;
import org.example.housing_tracker.models.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LocationJdbcTemplateRepositoryTest {
    @Autowired
    private LocationJdbcTemplateRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    static boolean hasSetup = false;

    @BeforeEach
    void setup() {
        if (!hasSetup) {
            hasSetup = true;
            jdbcTemplate.update("call set_known_good_state();");
        }
    }
    @Test
    void shouldFindAll () {
        List<Location> allLocations = repository.findAll();

        assertNotNull(allLocations);
        assertEquals(allLocations.size(),2);
    }

}