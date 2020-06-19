package pl.poul12.matchzone.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.poul12.matchzone.model.Comment;
import pl.poul12.matchzone.model.PersonalDetails;
import pl.poul12.matchzone.service.CommentServiceImpl;
import pl.poul12.matchzone.service.PersonalDetailsServiceImpl;
import pl.poul12.matchzone.util.CustomErrorResponse;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("api/v1")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private CommentServiceImpl commentService;
    private PersonalDetailsServiceImpl personalDetailsService;

    public CommentController(CommentServiceImpl commentService, PersonalDetailsServiceImpl personalDetailsService) {
        this.commentService = commentService;
        this.personalDetailsService = personalDetailsService;
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<List<Comment>> getAllByUser(@PathVariable(value = "id") Long id){

        List<Comment> comments = commentService.getAllByUser(id);
        return ResponseEntity.ok().body(comments);

    }

    @PostMapping("/comments/{id}")
    public ResponseEntity<?> addComment(@PathVariable(value = "id") Long id, @RequestBody Comment comment) {

        PersonalDetails personalDetails = personalDetailsService.getPersonalDetails(id);
        comment.setAvatar(personalDetails.getPhoto());

        commentService.createComment(id, comment);

        return new ResponseEntity<>(new CustomErrorResponse("Comment created successfully"), HttpStatus.OK);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable(value = "id") Long commentId, @Valid @RequestBody Comment comment) {

        final Comment updatedComment = commentService.editComment(commentId, comment);

        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> removeComment(@PathVariable(value = "id") Long commentId){

        boolean isRemoved = commentService.deleteComment(commentId);

        if(isRemoved){
            return new ResponseEntity<>(new CustomErrorResponse("Comment removed successfully"), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new CustomErrorResponse("Comment cannot be removed"), HttpStatus.BAD_REQUEST);
        }
    }
}
