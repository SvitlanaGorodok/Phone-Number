package api.phonecontacts.repository;

import api.phonecontacts.model.dao.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, UUID> {
    Optional<PhoneNumber> findByNumber(String number);
}
