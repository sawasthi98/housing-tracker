package org.example.housing_tracker.domain;

import org.example.housing_tracker.data.CommentRepository;
import org.example.housing_tracker.models.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private CommentRepository repository;

    public CommentService(CommentRepository repository) {
        this.repository = repository;
    }

    public List<Comment> findAllByListingId (int listingId) {
        return repository.findAllByListingId(listingId);
    }

    public Result<Comment> addComment (Comment comment) {
        Result<Comment> result = new Result();

        if (!result.isSuccess()) {
            return result;
        }

        if (comment.getCommentId() != 0) {
            result.addErrorMessage("Comment ID cannot be set for `add` operation.",ResultType.INVALID);
            return result;
        }

        comment = repository.addComment(comment);
        result.setPayload(comment);
        return result;
    }

    public Result<Comment> updateComment (Comment comment) {
        Result<Comment> result = validate(comment);

        if (!result.isSuccess()) {
            return result;
        }

        if (comment.getCommentId() != 0) {
            result.addErrorMessage("Comment ID must be set for `update` operation",ResultType.INVALID);
            return result;
        }

        if (!repository.updateComment(comment)) {
            String msg = String.format("commentId %s not found",comment.getCommentId());
            result.addErrorMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteCommentById (int commentId) {
        return repository.deleteCommentById(commentId);
    }

    public Result<Comment> validate (Comment comment) {
        Result result = new Result();

        if (comment.getCommentText() == null || comment.getCommentText().isBlank()) {
            result.addErrorMessage("Comment cannot be left null or blank.", ResultType.INVALID);
        }

        if (comment.getListingId() == 0) {
            result.addErrorMessage("Listing ID must be larger than 0.",ResultType.INVALID);
        }

        return result;

    }
}
