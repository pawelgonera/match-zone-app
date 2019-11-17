package pl.poul12.matchzone.controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Test;
import pl.poul12.matchzone.config.ConfigControllerBeans;
import pl.poul12.matchzone.model.PersonalDetails;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.service.PersonalDetailsService;
import pl.poul12.matchzone.service.UserService;
import pl.poul12.matchzone.util.MailSender;

import javax.mail.MessagingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserController.class)
@Import(ConfigControllerBeans.class)
public class UserControllerTest {

    private static final User USER_TEST = new User();

    @Autowired
    private MailSender mailSender;

    @Autowired
    private UserController userController;

    @Autowired
    private PersonalDetailsService personalDetailsService;

    @Autowired
    private UserService userService;

    @Before
    public void setUp(){
        USER_TEST.setId(1L);
        USER_TEST.setUsername("Pablito01");
        USER_TEST.setFirstName("Pablo01");
        USER_TEST.setEmail("mail01@g.com");
        USER_TEST.setPassword("pass01");
    }

    @Test
    public void shouldGetOk200ResponseWhenResetPass() throws MessagingException {

        //given
        final HttpStatus responseEntityOk = HttpStatus.OK;

        ///when
        doReturn(USER_TEST).when(userService).getUserByEmail(USER_TEST.getEmail());

        when(mailSender.sendEmail(eq(USER_TEST.getEmail()), anyString())).thenReturn(anyString());

        final ResponseEntity responseEntityTest = userController.resetPassword(USER_TEST.getEmail());

        //then
        assertEquals(responseEntityOk, responseEntityTest.getStatusCode());
    }

    @Test
    public void shouldGet400BadRequestResponseWhenResetPass() throws MessagingException {

        //given
        final HttpStatus responseEntityBadRequest = HttpStatus.BAD_REQUEST;

        ///when
        doReturn(USER_TEST).when(userService).getUserByEmail(USER_TEST.getEmail());

        when(mailSender.sendEmail(eq(USER_TEST.getEmail()), anyString())).thenThrow(MessagingException.class);

        final ResponseEntity responseEntityTest = userController.resetPassword(USER_TEST.getEmail());

        //then
        assertEquals(responseEntityBadRequest, responseEntityTest.getStatusCode());
    }

    @Test
    public void shouldGetOk200ResponseWhenSavePhoto() {
        //given
        final int fileSizeTest = 1024;
        final byte[] byteDataTest = new byte[fileSizeTest];
        final String name = "Photo Name";
        final String fileName = "image.png";
        final String contentType = "image/png";
        MockMultipartFile fileTest = new MockMultipartFile(name, fileName, contentType, byteDataTest);
        final HttpStatus responseEntityOk = HttpStatus.OK;

        //when
        doReturn(new PersonalDetails()).when(personalDetailsService).getPersonalDetails(USER_TEST.getUsername());

        final ResponseEntity responseEntityTest = userController.changeAvatar(USER_TEST.getUsername(), fileTest);

        //then
        assertEquals(responseEntityOk, responseEntityTest.getStatusCode());

    }

    @Test
    public void shouldGet400BadRequestResponseWhenSavePhoto(){
        //given
        final int fileSizeWrong = 12_000_000;
        final byte[] byteDataTest = new byte[fileSizeWrong];
        final String name = "Photo Name";
        final String fileName = "image.png";
        final String contentType = "image/png";
        MockMultipartFile fileTest = new MockMultipartFile(name, fileName, contentType, byteDataTest);
        final HttpStatus responseEntityBadRequest = HttpStatus.BAD_REQUEST;

        //when
        doReturn(new PersonalDetails()).when(personalDetailsService).getPersonalDetails(USER_TEST.getUsername());

        final ResponseEntity responseEntityTest = userController.changeAvatar(USER_TEST.getUsername(), fileTest);

        //then
        assertEquals(responseEntityBadRequest, responseEntityTest.getStatusCode());
    }

    @Test
    public void shouldGet415UnsupportedMediaTypeResponseWhenSavePhoto(){
        //given
        final int fileSizeTest = 1024;
        final byte[] byteDataTest = new byte[fileSizeTest];
        final String name = "Photo Name";
        final String fileName = "image.png";
        final String contentType = "text/html";
        MockMultipartFile fileTest = new MockMultipartFile(name, fileName, contentType, byteDataTest);
        final HttpStatus responseEntityUnsupportedMediaType = HttpStatus.UNSUPPORTED_MEDIA_TYPE;

        //when
        doReturn(new PersonalDetails()).when(personalDetailsService).getPersonalDetails(USER_TEST.getUsername());

        final ResponseEntity responseEntityTest = userController.changeAvatar(USER_TEST.getUsername(), fileTest);

        //then
        assertEquals(responseEntityUnsupportedMediaType, responseEntityTest.getStatusCode());
    }
}