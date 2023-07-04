package api.phonecontacts.repository;

import api.phonecontacts.model.dao.ContactDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<ContactDao, UUID> {
}
