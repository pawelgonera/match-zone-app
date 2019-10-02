package pl.poul12.matchzone.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.*;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "users")
@Scope("session")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;
    @Column(unique = true)
    private String username;
    private String firstName;
    @Column(unique = true)
    private String email;
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;
    @Transient
    private String repeatedPassword;
    private String timeZoneId;
    @JsonManagedReference
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private PersonalDetails personalDetails;
    @JsonManagedReference
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private Appearance appearance;
    @JsonManagedReference
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Vote> votes;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

 }
