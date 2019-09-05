package pl.poul12.matchzone.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.poul12.matchzone.model.enums.MaritalStatus;
import pl.poul12.matchzone.model.enums.Religion;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Table(name = "personal_details")
class PersonalDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String country;
    private String city;
    private String occupation;
    private MaritalStatus maritalStatus;
    private String education;
    private Religion religion;
}
