package org.example.housing_tracker.data.mappers;

import org.example.housing_tracker.models.Comment;
import org.example.housing_tracker.models.Listing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentMapper implements RowMapper<Comment> {

    private JdbcTemplate jdbcTemplate;

    public CommentMapper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Comment comment = new Comment();
        comment.setCommentId(rs.getInt("comment_id"));
        comment.setListingId(rs.getInt("listing_id"));
        comment.setAppUserId(rs.getInt("app_user_id"));
        comment.setCommentText(rs.getString("comment_text"));

        return comment;
    }

}
