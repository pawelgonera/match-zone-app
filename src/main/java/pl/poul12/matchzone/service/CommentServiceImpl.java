package pl.poul12.matchzone.service;

import org.springframework.stereotype.Service;
import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.model.Comment;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private UserService userService;

    public CommentServiceImpl(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    public Comment getCommentById(Long id)  {
        Optional<Comment> commentFound = commentRepository.findById(id);
        return commentFound.orElseThrow(() -> new ResourceNotFoundException("Comment not found for this id: " + id));
    }

    public List<Comment> getCommentsByAuthor(String author)  {
        List<Comment> comments = commentRepository.findByAuthor(author);
        if(comments.isEmpty())
        {
            throw new ResourceNotFoundException("Comments not found for this id: " + author);
        }else {
            return comments;
        }
    }

    public List<Comment> getAllByUser(String username){
        User user = userService.getUserByUsername(username);
        return commentRepository.findAllByUser(user);
    }

    public Comment createComment(String username, Comment comment){
        User user = userService.getUserByUsername(username);
        comment.setUser(user);

        return commentRepository.save(comment);
    }

    public Comment editComment(Long commentId, Comment comment) {
        Comment commentFound = getCommentById(commentId);
        commentFound.setContent(comment.getContent());
        commentFound.setPostDate(comment.getPostDate());

        return commentRepository.save(commentFound);
    }

    public boolean deleteComment(Long commentId){
        try{
            commentRepository.deleteById(commentId);
        }catch (Exception e){
            return false;
        }

        return true;
    }
}

