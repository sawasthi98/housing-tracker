package org.example.housing_tracker.data;

import org.example.housing_tracker.models.AppUser;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Component
public interface AppUserRepository {
    @Transactional
    public AppUser findByUsername(String username);

    // create
    @Transactional
    public AppUser create(AppUser user);

    public List<AppUser>findAll();

    @Transactional
    public int findAppIdByUsername(String username);
    public AppUser findUserByAppUserId(int appUserId);


}
