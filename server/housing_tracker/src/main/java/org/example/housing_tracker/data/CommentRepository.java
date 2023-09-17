package org.example.housing_tracker.data;

import org.example.housing_tracker.models.Comment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface CommentRepository {

    public Comment findAll();
    public Comment addComment(Comment comment);
    public Comment updateComment(Comment comment);
    public boolean deleteComment(Comment comment);

}
