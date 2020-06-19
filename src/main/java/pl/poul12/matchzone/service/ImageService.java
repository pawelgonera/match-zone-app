package pl.poul12.matchzone.service;

import pl.poul12.matchzone.model.Image;

import java.util.List;

public interface ImageService {

    Image getImageById(Long id);

    List<Image> getAllByUser(Long userId);

    Image createImage(String username, Image image);

    Image editImage(Long imageId, String title);

    boolean deleteImage(Long imageId);
}
