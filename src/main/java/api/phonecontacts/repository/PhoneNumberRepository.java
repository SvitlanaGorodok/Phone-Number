package api.phonecontacts.repository;

import api.phonecontacts.model.dao.PhoneNumberDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumberDao, UUID> {
}
