package org.example.housing_tracker.data.mappers;

import org.example.housing_tracker.models.AppUser;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AppUserMapper implements RowMapper<AppUser> {

    private final List<String> roles;

    public AppUserMapper(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        AppUser appUser=new AppUser();
        appUser.setAppUserId(rs.getInt("app_user_id"));
        appUser.setUsername(rs.getString("username"));
        appUser.setPassword(rs.getString("password_hash"));
        appUser.setEnabled(rs.getBoolean("enabled"));
        appUser.setAuthorities(AppUser.convertRolesToAuthorities(roles));
        return appUser;
    }
}
