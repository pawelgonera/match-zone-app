package pl.poul12.matchzone.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import pl.poul12.matchzone.model.enums.Eyes;
import pl.poul12.matchzone.model.enums.HairColour;
import pl.poul12.matchzone.model.enums.Physique;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
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
    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    User user;
}
