package api.phonecontacts.model.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Table(name = "phone_numbers")
@Entity
@Setter
@Getter
public class PhoneNumberDao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "number", nullable = false, length = 50, unique = true)
    private String number;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "phoneNumbers")
    private Set<ContactDao> contacts;
}
