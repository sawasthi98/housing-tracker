package org.example.housing_tracker.data;

import org.example.housing_tracker.models.Comment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
public interface CommentRepository {

    public List<Comment> findAll();
    public List<Comment> findByListingId(int listingId);
    public Comment addComment(Comment comment);
    public boolean updateComment(Comment comment);
    public boolean deleteCommentById(int commentId);

}
