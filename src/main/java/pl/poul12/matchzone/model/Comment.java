package pl.poul12.matchzone.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
public class Comment{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String author;
    @Type(type = "org.hibernate.type.ImageType")
    @Lob
    private byte[] avatar;
    private LocalDateTime postDate;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    @EqualsAndHashCode.Exclude
    private User user;

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", postDate=" + postDate +
                '}';
    }
}

