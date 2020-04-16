package pl.poul12.matchzone.service;

import pl.poul12.matchzone.model.Comment;

import java.util.List;

public interface CommentService {

    Comment getCommentById(Long id);

    List<Comment> getCommentsByAuthor(String author);

    List<Comment> getAllByUser(String username);

    Comment createComment(String username, Comment comment);

    Comment editComment(Long commentId, Comment comment);

    boolean deleteComment(Long commentId);
}
