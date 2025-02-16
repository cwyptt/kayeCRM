package io.github.cwyptt.crm.repository;

import io.github.cwyptt.crm.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static io.github.cwyptt.crm.utility.constant.QueryConstants.PRIMARY_CONTACT_QUERY;
import static io.github.cwyptt.crm.utility.constant.QueryConstants.SEARCH_CONTACTS_QUERY;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByCustomerId(Long customerId);
    Optional<Contact> findByEmailAndCustomerId(String email, Long customerId);

    @Query(PRIMARY_CONTACT_QUERY)
    Optional<Contact> findPrimaryContactByCustomerId(@Param("customerId") Long customerId);

    boolean existsByCustomerId(Long customerId);
    boolean existsByEmailAndCustomerId(String email, Long customerId);

    @Query(SEARCH_CONTACTS_QUERY)
    List<Contact> searchContacts(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("position") String position,
            @Param("department") String department,
            @Param("customerId") Long customerId
    );
}
