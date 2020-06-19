package pl.poul12.matchzone.service;

import pl.poul12.matchzone.model.Comment;

import java.util.List;

public interface CommentService {

    Comment getCommentById(Long id);

    List<Comment> getCommentsByAuthor(String author);

    List<Comment> getAllByUser(Long userId);

    Comment createComment(Long userId, Comment comment);

    Comment editComment(Long commentId, Comment comment);

    boolean deleteComment(Long commentId);
}
