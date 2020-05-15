package pl.poul12.matchzone.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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
@Table(name = "users",
        indexes = {
                @Index(name = "firstnameindex",  columnList="firstName")
        }
)
//@Scope("session")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @Fetch(FetchMode.SELECT)
    private PersonalDetails personalDetails;
    @JsonManagedReference
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @Fetch(FetchMode.SELECT)
    private Appearance appearance;
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @Fetch(FetchMode.JOIN)
    private Set<Vote> votes;
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @Fetch(FetchMode.JOIN)
    private Set<Comment> comments;
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @Fetch(FetchMode.JOIN)
    private Set<Image> images;
    @JsonManagedReference
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER,  cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    private Set<Message> messages;
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", repeatedPassword='" + repeatedPassword + '\'' +
                ", timeZoneId='" + timeZoneId + '\'' +
                '}';
    }
}
