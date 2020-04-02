package pl.poul12.matchzone.model.dto;

import org.springframework.web.multipart.MultipartFile;

public class ImageDto {

    private Long id;
    private MultipartFile photo;
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
