package io.github.cwyptt.crm.repository;

import io.github.cwyptt.crm.entity.Customer;
import io.github.cwyptt.crm.enums.CustomerStatus;
import io.github.cwyptt.crm.utility.constant.QueryConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Contact and Company related queries
    List<Customer> findByContactId(Long contactId);
    List<Customer> findByCompanyId(Long companyId);

    // Status-based queries
    List<Customer> findByStatus(CustomerStatus status);

    List<Customer> findByCreatedAtGreaterThanEqual(LocalDateTime since);

    @Query(QueryConstants.SEARCH_CUSTOMERS_QUERY)
    List<Customer> searchCustomers(
            @Param("status") CustomerStatus status,
            @Param("companyId") Long companyId,
            @Param("contactId") Long contactId,
            @Param("since") LocalDateTime since
    );
}
