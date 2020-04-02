package pl.poul12.matchzone.service;

import org.springframework.stereotype.Service;
import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.model.Image;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.model.dto.mapper.ImageDtoMapper;
import pl.poul12.matchzone.repository.ImageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService{

    private ImageRepository imageRepository;
    private UserService userService;
    private ImageDtoMapper mapper;

    public ImageServiceImpl(ImageRepository imageRepository, UserService userService) {
        this.imageRepository = imageRepository;
        this.userService = userService;
    }

    @Override
    public Image getImageById(Long id) {
        Optional<Image> imageFound = imageRepository.findById(id);
        return imageFound.orElseThrow(() -> new ResourceNotFoundException("Image not found for this id: " + id));
    }

    @Override
    public List<Image> getAllByUser(String username) {
        User user = userService.getUserByUsername(username);
        return imageRepository.findAllByUser(user);
    }

    @Override
    public Image createImage(String username, Image image) {
        User user = userService.getUserByUsername(username);
        image.setUser(user);

        return imageRepository.save(image);
    }

    @Override
    public Image editImage(Long imageId, String title) {
        Image imageFound = getImageById(imageId);
        imageFound.setTitle(title);

        return imageRepository.save(imageFound);
    }

    @Override
    public boolean deleteImage(Long imageId) {
        try{
            imageRepository.deleteById(imageId);
        }catch (Exception e){
            return false;
        }

        return true;
    }
}
