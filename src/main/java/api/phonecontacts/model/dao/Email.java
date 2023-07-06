package api.phonecontacts.model.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Table(name = "emails")
@Entity
@Setter
@Getter
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "emails")
    private Set<ContactDao> contacts;
}
