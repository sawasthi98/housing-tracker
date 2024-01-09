package org.example.housing_tracker.domain;

import org.apache.coyote.Request;
import org.example.housing_tracker.data.CommentRepository;
import org.example.housing_tracker.models.Comment;
import org.example.housing_tracker.models.Listing;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    CommentRepository repository;

    public CommentService(CommentRepository repository) {
        this.repository = repository;
    }

    public List<Comment> findAll() {
        return repository.findAll();
    }

    public List<Comment> findCommentsByListingId (int listingId) {
        return repository.findByListingId(listingId);
    }

    public Result<Comment> addComment (Comment comment) {
        Result<Comment> result = validate(comment);

        if (!result.isSuccess()) {
            return result;
        }

        if (comment.getCommentId() != 0) {
            result.addErrorMessage("commentId cannot be set for the 'add' operation", ResultType.INVALID);
            return result;
        }

//        if (repository.addComment(comment) == null) {
//            result.addErrorMessage("Unable to add new comment",ResultType.INVALID);
//            return result;
//        }

        comment = repository.addComment(comment);
        if (comment != null) {
            result.setPayload(comment);
        }

        return result;
    }

    public Result<Comment> updateComment (Comment comment) {
        Result<Comment> result = validate(comment);

        if (comment.getCommentId() <= 0) {
            result.addErrorMessage("commentId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!repository.updateComment(comment)) {
            String msg = String.format("commentId: %s, not found", comment.getCommentId());
            result.addErrorMessage(msg, ResultType.NOT_FOUND);
        }

        if (!repository.updateComment(comment)) {
            result.addErrorMessage("Unable to update comment", ResultType.INVALID);
            return result;
        }

        return result;
    }

    public boolean deleteCommentById (int commentId) {
        return repository.deleteCommentById(commentId);
    }

    private Result<Comment> validate (Comment comment) {
        Result<Comment> result = new Result<>();

        if (comment == null) {
            result.addErrorMessage("Comment cannot be null or blank",ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(comment.getCommentText())) {
            result.addErrorMessage("Comment is required and cannot be left blank", ResultType.INVALID);
        }

        List<Comment> all = repository.findAll();

        for (Comment c : all) {
            if (c.equals(comment)) {
                result.addErrorMessage("Comment already exists for this listing", ResultType.INVALID);
            }
        }

        return result;
    }
}
