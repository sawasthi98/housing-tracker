package org.example.housing_tracker.data;

import org.example.housing_tracker.data.mappers.AppUserMapper;
import org.example.housing_tracker.models.AppUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class AppUserJdbcTemplateRepository implements AppUserRepository {

    private JdbcTemplate jdbcTemplate;
    public AppUserJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public AppUser findByUsername(String username) {
        List<String> roles = getRolesByUsername(username);

        final String sql = "select app_user_id, username, password_hash, enabled "
                + "from app_user "
                + "where username = ?;";

        return jdbcTemplate.query(sql, new AppUserMapper(roles), username)
                .stream()
                .findFirst().orElse(null);
    }

    @Override
    @Transactional
    public AppUser create(AppUser user){

        final String sql = "insert into app_user (username, password_hash) values (?, ?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }
        int appUserId = keyHolder.getKey().intValue();
        user.setAppUserId(appUserId);
        //int appRoleId = user.getAuthorities().contains(new SimpleGrantedAuthority("USER")) ? 1 : 2;
        insertUserRole(appUserId, 2);

        return user;
    }

    @Override
    public List<AppUser> findAll()  {
        final String sql = "select app_user_id, username, password_hash, enabled from app_user;";

        List<String> roles = findAllRoles();
        return jdbcTemplate.query(sql, new AppUserMapper(roles));
    }

    @Override
    public int findAppIdByUsername(String username){
        AppUser user = findByUsername(username);
        return user.getAppUserId();
    }

    @Override
    public AppUser findUserByAppUserId(int appUserId) {

        final String sql = "select app_user_id, username, password_hash, enabled " +
                "from app_user " +
                "where app_user_id = ?;";

        List<String> roles = findAllRoles();

        List<AppUser> users = jdbcTemplate.query(sql, new AppUserMapper(roles), appUserId);

        if (users.isEmpty()) {
            return null;
        }

        // Manually create the AppUser object with only app_user_id field
        AppUser user = new AppUser();
        user.setAppUserId(appUserId);

        return user;
    }

    private List<String> getRolesByUsername(String username) {
        final String sql = "select r.name "
                + "from app_user_role ur "
                + "inner join app_role r on ur.app_role_id = r.app_role_id "
                + "inner join app_user au on ur.app_user_id = au.app_user_id "
                + "where au.username = ?";
        return jdbcTemplate.query(sql, (rs, rowId) -> rs.getString("name"), username);
    }

    private List<String> findAllRoles(){
        final String sql = "select role.name " +
                "            from app_user user " +
                "            inner join app_user_role user_role on user.app_user_id=user_role.app_user_id " +
                "            inner join app_role role on role.app_role_id=user_role.app_role_id ;";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    private boolean insertUserRole(int appUserId, int appRoleId) {
        String sql = "insert into app_user_role (app_user_id, app_role_id) values (?, ?);";
        return jdbcTemplate.update(sql, appUserId, appRoleId)>=1;
    }


}
