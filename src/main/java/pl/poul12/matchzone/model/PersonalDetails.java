package pl.poul12.matchzone.model;

import lombok.*;
import pl.poul12.matchzone.model.enums.MaritalStatus;
import pl.poul12.matchzone.model.enums.Religion;

import javax.persistence.*;
import java.time.LocalDate;

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
    @Basic(fetch=FetchType.EAGER)
    @Lob
    private byte[] photo;
    private Double rating;
    private String country;
    private String city;
    private String occupation;
    private MaritalStatus maritalStatus;
    private String education;
    private Religion religion;
    //@JsonBackReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    User user;
}
