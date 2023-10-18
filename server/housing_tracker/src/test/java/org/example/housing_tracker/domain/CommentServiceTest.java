package org.example.housing_tracker.domain;

import org.example.housing_tracker.data.CommentRepository;
import org.example.housing_tracker.models.Comment;
import org.example.housing_tracker.models.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    CommentService service;

    @MockBean
    CommentRepository repository;

    @Test
    void shouldFindAllCommentsByListingId () {
        when(repository.findByListingId(2)).thenReturn(List.of(
                new Comment(1, "Not a safe area", 4, 2)
        ));

        List<Comment> all = service.findCommentsByListingId(2);

        assertEquals(1,all.size());
    }

    @Test
    void shouldAddNewComment () {
        Comment comment = new Comment();

        comment.setCommentText("So much sunlight! I love it :)");
        comment.setAppUserId(2);
        comment.setListingId(2);

        when(repository.addComment(comment)).thenReturn(comment);

        Result<Comment> result = service.addComment(comment);

        assertTrue(result.isSuccess());
        assertNotNull(result.getErrorMessages());
    }

    @Test
    void shouldNotAddDuplicateComments () {
        Comment comment = new Comment(3,"Test comment",2,1);

        when(repository.addComment(comment)).thenReturn(null);

        Result<Comment> result = service.addComment(comment);

        assertNotNull(result);
        assertFalse(result.isSuccess());
//        assertEquals("Comment already exists",result.getErrorMessages());
    }

    @Test
    void shouldNotAddNullOrBlankComment () {
        Comment comment = new Comment(19, "Null comment", 1,2);

        Result<Comment> result = service.addComment(comment);
        assertEquals(ResultType.INVALID, result.getResultType());

        comment.setCommentId(0);
        comment.setCommentText(null);

        result = service.addComment(comment);
        assertEquals(ResultType.INVALID, result.getResultType());

        comment.setCommentText("     ");
        result = service.addComment(comment);
        assertEquals(ResultType.INVALID, result.getResultType());

        comment.setCommentText("");
        result = service.addComment(comment);
        assertEquals(ResultType.INVALID, result.getResultType());
    }

    @Test
    void shouldUpdateComment () {
        
    }

    @Test
    void shouldNotUpdateCommentIfNullOrBlank () {

    }

    @Test
    void shouldDeleteCommentByCommentId () {

    }
}