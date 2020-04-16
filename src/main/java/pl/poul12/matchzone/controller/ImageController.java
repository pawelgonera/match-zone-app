package pl.poul12.matchzone.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.poul12.matchzone.model.Image;
import pl.poul12.matchzone.service.ImageServiceImpl;
import pl.poul12.matchzone.util.CustomErrorResponse;
import pl.poul12.matchzone.util.FileValidator;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("api/v1")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private FileValidator validator = new FileValidator();

    private ImageServiceImpl imageService;

    public ImageController(ImageServiceImpl imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/images/{username}")
    public ResponseEntity<List<Image>> getAllByUser(@PathVariable(value = "username") String username){

        List<Image> images = imageService.getAllByUser(username);
        return ResponseEntity.ok().body(images);

    }

    @PostMapping("/images/{username}")
    public ResponseEntity<?> addImages(@PathVariable(value = "username") String username, @RequestParam("photo") MultipartFile photo, @RequestParam("title") String title) {

        Image image = new Image();
        try {

            try {
                return validator.validate(photo);
            }catch (RuntimeException e){
                image.setPhoto(photo.getBytes());
            }

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Something went wrong with your file" + photo.getOriginalFilename());
        }
        image.setTitle(title);

        imageService.createImage(username, image);

        return new ResponseEntity<>(new CustomErrorResponse("Image created successfully"), HttpStatus.OK);
    }

    @PutMapping("/images/{id}")
    public ResponseEntity<Image> updateImage(@PathVariable(value = "id") Long imageId, @RequestBody Image image) {

        final Image updatedImage = imageService.editImage(imageId, image.getTitle());

        return ResponseEntity.ok(updatedImage);
    }

    @DeleteMapping("/images/{id}")
    public ResponseEntity<?> removeImage(@PathVariable(value = "id") Long imageId){

        boolean isRemoved = imageService.deleteImage(imageId);

        if(isRemoved){
            return new ResponseEntity<>(new CustomErrorResponse("Image removed successfully"), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new CustomErrorResponse("Image cannot be removed"), HttpStatus.BAD_REQUEST);
        }
    }
}