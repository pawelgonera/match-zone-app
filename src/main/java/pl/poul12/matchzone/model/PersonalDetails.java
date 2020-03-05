package pl.poul12.matchzone.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.Type;
import pl.poul12.matchzone.model.enums.Gender;
import pl.poul12.matchzone.model.enums.MaritalStatus;
import pl.poul12.matchzone.model.enums.Religion;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Arrays;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "personal_details")
public class PersonalDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dateOfBirth;
    private Integer age;
    private Gender gender;
    @Type(type = "org.hibernate.type.ImageType")
    @Lob
    private byte[] photo;
    private Double rating;
    private String country;
    private String city;
    private String occupation;
    private MaritalStatus maritalStatus;
    private String education;
    private Religion religion;
    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    User user;

    @Override
    public String toString() {
        return "PersonalDetails{" +
                "id=" + id +
                ", dateOfBirth=" + dateOfBirth +
                ", age=" + age +
                ", gender=" + gender +
                ", rating=" + rating +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", occupation='" + occupation + '\'' +
                ", maritalStatus=" + maritalStatus +
                ", education='" + education + '\'' +
                ", religion=" + religion +
                '}';
    }
}
