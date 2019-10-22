package pl.poul12.matchzone.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "mail_data")
public class MailData
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mail_id", nullable = false)
    private Long id;
    @Column(nullable = false, length = 25)
    private String addressName;
    @Column(nullable = false, length = 128)
    private String password;
}
