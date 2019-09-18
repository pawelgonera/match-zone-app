package pl.poul12.matchzone.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;
    @Transient
    private String repeatedPassword;
    private String timeZoneId;
    //@OneToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "id_personal_details")
    @JsonManagedReference
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PersonalDetails personalDetails;
    //@OneToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "id_appearance")
    @JsonManagedReference
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Appearance appearance;

}
