package org.example.housing_tracker.controllers;

import org.apache.coyote.Response;
import org.example.housing_tracker.domain.AppUserService;
import org.example.housing_tracker.domain.CommentService;
import org.example.housing_tracker.domain.Result;
import org.example.housing_tracker.models.AppUser;
import org.example.housing_tracker.models.Comment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/listingId/")
public class CommentController {

    private final CommentService service;
    private final AppUserService appUserService;

    public CommentController(CommentService service, AppUserService appUserService) {
        this.service = service;
        this.appUserService = appUserService;
    }

    @GetMapping("/{listingId}")
    public List<Comment> findAll(@PathVariable int listingId) {
        return service.findCommentsByListingId(listingId);
    }

    @PostMapping("/comments")
    public ResponseEntity<Object> addComment(@RequestBody Comment comment) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = appUserService.loadUserByUsername(username);

        comment.setAppUserId(user.getAppUserId());

        Result<Comment> result = service.addComment(comment);

        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Object> updateComment(@RequestBody Comment comment, @PathVariable int commentId) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = appUserService.loadUserByUsername(username);

        comment.setAppUserId(user.getAppUserId());
        comment.setCommentId(commentId);

        Result<Comment> result = service.updateComment(comment);

        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteById(@PathVariable int commentId) {
        if (service.deleteCommentById(commentId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
