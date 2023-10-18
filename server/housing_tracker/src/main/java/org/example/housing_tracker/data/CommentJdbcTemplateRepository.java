package org.example.housing_tracker.data;

import org.example.housing_tracker.data.mappers.CommentMapper;
import org.example.housing_tracker.data.mappers.ListingMapper;
import org.example.housing_tracker.models.Comment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CommentJdbcTemplateRepository implements CommentRepository {
    private JdbcTemplate jdbcTemplate;

    public CommentJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Comment> findAll() {
        final String sql = "select comment_id, comment_text, listing_id, app_user_id " +
                "from comments; " ;

        return jdbcTemplate.query(sql, new CommentMapper(jdbcTemplate));
    }

    @Override
    public List<Comment> findByListingId(int listingId) {
        final String sql = "select comment_id, comment_text, listing_id, app_user_id " +
                "from comments " +
                "where listing_id = ?;";

        return jdbcTemplate.query(sql, new CommentMapper(jdbcTemplate), listingId);
    }

    @Override
    public Comment addComment (Comment comment) {
        final String sql = "insert into comments (comment_text, app_user_id, listing_id) " +
                "values  (?, ?, ?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, comment.getCommentText());
            ps.setInt(2, comment.getAppUserId());
            ps.setInt(3, comment.getListingId());

            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }
        int commentId = keyHolder.getKey().intValue();
        comment.setCommentId(commentId);

        return comment;
    }

    @Override
    public boolean updateComment (Comment comment) {
        final String sql = "update comments set " +
                "comment_text = ? " +
                "where listing_id = ? " +
                "and app_user_id = ?;";

        return jdbcTemplate.update(sql,
                comment.getCommentText(),
                comment.getListingId(),
                comment.getAppUserId())
                > 0;
    }

    @Override
    public boolean deleteCommentById (int commentId) {
        return jdbcTemplate.update("delete from comments where comment_id = ?;", commentId) > 0;
    }
}
