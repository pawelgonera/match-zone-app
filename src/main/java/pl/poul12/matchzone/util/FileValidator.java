package pl.poul12.matchzone.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import pl.poul12.matchzone.controller.CommentController;
import pl.poul12.matchzone.model.PersonalDetails;
import pl.poul12.matchzone.service.PersonalDetailsService;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

public class FileValidator {

    private final static Long MAX_FILE_SIZE = 10_000_000L;
    private static String IS_IMAGE_TYPE = Pattern.compile("image/.+").pattern();

    private static final Logger logger = LoggerFactory.getLogger(FileValidator.class);

    private PersonalDetailsService personalDetailsService;

    public ResponseEntity<?> validate(MultipartFile file){

            if(file.getSize() > MAX_FILE_SIZE)
            {
                return new ResponseEntity<>(new CustomErrorResponse("File size is too large, maximum size is 10 MB"), HttpStatus.BAD_REQUEST);
            }

            if(!Objects.requireNonNull(file.getContentType()).matches(IS_IMAGE_TYPE)){
                return new ResponseEntity<>(new CustomErrorResponse("Media type not required, it must be an image type"), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            }

            throw new RuntimeException();
    }

}
