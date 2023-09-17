package org.example.housing_tracker.data;

import org.example.housing_tracker.models.Comment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CommentJdbcTemplateRepository implements CommentRepository {
    private JdbcTemplate jdbcTemplate;

    public CommentJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Comment findAll() {
        return null;
    }

    @Override
    public Comment addComment(Comment comment) {
        return null;
    }

    @Override
    public Comment updateComment(Comment comment) {
        return null;
    }

    @Override
    public boolean deleteComment(Comment comment) {
        return false;
    }
}
