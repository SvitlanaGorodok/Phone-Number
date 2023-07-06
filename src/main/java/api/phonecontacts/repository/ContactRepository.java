package api.phonecontacts.repository;

import api.phonecontacts.model.dao.ContactDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<ContactDao, UUID> {
    @Query(value = "SELECT c.id, c.name, c.user_id FROM contacts c WHERE c.name = :name AND c.user_id = :userId", nativeQuery = true)
    List<ContactDao> findByNameAndUserId(@Param("name") String name, @Param("userId") UUID userId);
}
