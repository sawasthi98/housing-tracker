package org.example.housing_tracker.controllers;


import org.apache.coyote.Response;
import org.example.housing_tracker.domain.CommentService;
import org.example.housing_tracker.domain.Result;
import org.example.housing_tracker.models.Comment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/{listingId}/")
public class CommentController {

    private final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Comment> findAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<Object> addComment(@RequestBody Comment comment) {
        Result<Comment> result = service.addComment(comment);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteById(@PathVariable int commentId) {
        if (service.deleteCommentById(commentId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
