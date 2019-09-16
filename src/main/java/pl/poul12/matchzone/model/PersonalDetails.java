package pl.poul12.matchzone.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.poul12.matchzone.model.enums.MaritalStatus;
import pl.poul12.matchzone.model.enums.Religion;

import javax.persistence.*;
import java.sql.Blob;

@NoArgsConstructor
@Data
@Entity
@Table(name = "personal_details")
public class PersonalDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private Integer age;
    @Lob
    private byte[] photo;
    private String country;
    private String city;
    private String occupation;
    private MaritalStatus maritalStatus;
    private String education;
    private Religion religion;
}
