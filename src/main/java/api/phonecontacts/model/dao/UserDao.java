package api.phonecontacts.model.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Table(name = "users")
@Entity
@Setter
@Getter
public class UserDao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "login", nullable = false, length = 50, unique = true)
    private String login;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<ContactDao> contacts = new HashSet<>();

}
