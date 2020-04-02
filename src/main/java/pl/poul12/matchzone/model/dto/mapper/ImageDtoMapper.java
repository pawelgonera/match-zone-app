package pl.poul12.matchzone.model.dto.mapper;

import pl.poul12.matchzone.model.Image;
import pl.poul12.matchzone.model.dto.ImageDto;

import java.io.IOException;

public class ImageDtoMapper {

    public Image toImage(ImageDto imageDto) throws IOException {
        Image image = new Image();
        image.setId(imageDto.getId());
        image.setPhoto(imageDto.getPhoto().getBytes());
        image.setTitle(imageDto.getTitle());

        return image;
    }
}
