package pl.poul12.matchzone.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import pl.poul12.matchzone.config.ConfigBeans;
import pl.poul12.matchzone.model.Comment;
import pl.poul12.matchzone.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserServiceImpl.class)
@Import(ConfigBeans.class)
public class CommentServiceImplTest {
    private static final Comment COMMENT_TEST = new Comment();

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Before
    public void setUp(){
        COMMENT_TEST.setId(1L);
        COMMENT_TEST.setAuthor("Pablito01");
        COMMENT_TEST.setContent("Hejka");
        COMMENT_TEST.setPostDate(LocalDateTime.now());
    }

    @Test
    public void shouldReturnCommentByUAuthor() {

        //given
        List<Comment> comments = Collections.singletonList(COMMENT_TEST);
        doReturn(comments).when(commentRepository).findByAuthor(COMMENT_TEST.getAuthor());

        //when
        final List<Comment> commentsFound = commentService.getCommentsByAuthor(COMMENT_TEST.getAuthor());

        //then
        assertEquals(comments, commentsFound);
    }

}
