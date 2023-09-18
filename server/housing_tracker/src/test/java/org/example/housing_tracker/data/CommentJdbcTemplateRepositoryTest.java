package org.example.housing_tracker.data;

import org.example.housing_tracker.models.Comment;
import org.example.housing_tracker.models.Listing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentJdbcTemplateRepositoryTest {

    @Autowired
    private CommentJdbcTemplateRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Comment testComment = new Comment(1,"Not a safe area", 4, 2);

    private Comment newComment = new Comment();

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
        List<Comment> allComments = repository.findAll();

        assertNotNull(allComments);
        assertEquals(allComments.size(),2);
    }

//    @Test
//    void shouldNotFindNonexistentComment () {
//        Comment nonexistentComment = repository.findCo(3000);
//
//        assertNull(nonexistentComment);
//    }

    @Test
    void shouldAddComment () {
        newComment.setCommentId(3);
        newComment.setCommentText("Test comment");
        newComment.setAppUserId(3);
        newComment.setListingId(2);

        Comment createdListing = repository.addComment(newComment);

        assertNotNull(createdListing);
        assertEquals();
    }

    @Test
    void shouldUpdateComment () {

    }

    @Test
    void shouldDeleteCommentById () {

    }

}