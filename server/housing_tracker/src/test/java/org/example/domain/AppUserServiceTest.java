package org.example.domain;

import org.example.housing_tracker.data.AppUserJdbcTemplateRepository;
import org.example.housing_tracker.domain.AppUserService;
import org.example.housing_tracker.domain.Result;
import org.example.housing_tracker.models.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class AppUserServiceTest {

    @Autowired
    AppUserService appUserService;

    @MockBean
    AppUserJdbcTemplateRepository repository;

    @Test
    void shouldFindJohnSmith () {
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        String username = "john@smith.com";
        when(repository.findByUsername(username)).thenReturn(new AppUser(1,"john@smith.com","$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa",true,roles));

        AppUser foundUser = appUserService.loadUserByUsername(username);

        assertEquals(foundUser.getUsername(),username);
    }

    @Test
    void shouldCreateNewUser () {
        List<String> roles = new ArrayList<>();
        roles.add("USER");

        AppUser newUser = new AppUser(1, "new_user@user.com","$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa",true,roles);


        AppUser createdKareem = repository.create(newUser);

        when(repository.create(createdKareem)).thenReturn(newUser);

        Result result = appUserService.create(newUser.getUsername(),newUser.getPassword());

        assertTrue(result.isSuccess());
        assertNotNull(result.getErrorMessages());
    }

    @Test
    void shouldNotDuplicateNewUser () {
        List<String> roles = new ArrayList<>();
        roles.add("USER");

        AppUser newUser = new AppUser(1, "new_user@user.com","$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa",true,roles);

        when(repository.findByUsername(newUser.getUsername())).thenReturn(newUser);

        Result result = appUserService.create(newUser.getUsername(), newUser.getPassword());

        assertFalse(result.isSuccess());
        assertNotNull(result.getErrorMessages());
        assertTrue(result.getErrorMessages().contains("Username already exists. Choose another one."));
    }

    @Test
    void shouldNotCreateBlankOrNullUser () {
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        AppUser kareem = new AppUser(1, " ","$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa",true,roles);
        AppUser nullKareem = new AppUser(1, null,"$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa",true,roles);

        when(repository.create(kareem)).thenReturn(kareem);
        when(repository.create(kareem)).thenReturn(nullKareem);

        Result result = appUserService.create(kareem.getUsername(),kareem.getPassword());
        Result result1 = appUserService.create(nullKareem.getUsername(),nullKareem.getPassword());

        assertFalse(result.isSuccess());
        assertNotNull(result.getErrorMessages());
        assertTrue(result.getErrorMessages().contains("Username is required."));

        assertFalse(result1.isSuccess());
        assertNotNull(result1.getErrorMessages());
        assertTrue(result1.getErrorMessages().contains("Username is required."));

    }


}