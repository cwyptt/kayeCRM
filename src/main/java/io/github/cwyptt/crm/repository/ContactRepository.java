package io.github.cwyptt.crm.repository;

import io.github.cwyptt.crm.entity.Contact;
import io.github.cwyptt.crm.utility.constant.QueryConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    boolean existsByEmail(String email);
    Optional<Contact> findByEmail(String email);

    // Company-related queries
    List<Contact> findByCompanyId(Long companyId);

    @Query(QueryConstants.SEARCH_CONTACTS_QUERY)
    List<Contact> searchContacts(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("position") String position,
            @Param("department") String department,
            @Param("companyId") Long companyId,
            @Param("isCustomer") Boolean isCustomer
    );

}
