package api.phonecontacts.repository;

import api.phonecontacts.model.dao.EmailDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmailRepository extends JpaRepository<EmailDao, UUID> {
}
