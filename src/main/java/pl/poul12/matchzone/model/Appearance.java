package pl.poul12.matchzone.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.poul12.matchzone.model.enums.Eyes;
import pl.poul12.matchzone.model.enums.HairColour;
import pl.poul12.matchzone.model.enums.Physique;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
public class Appearance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Eyes eyes;
    private HairColour hairColour;
    private Short height;
    private Physique physique;
    private String about;
    private String hobbies;
    //@OneToOne(mappedBy = "appearance")
    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;
}
