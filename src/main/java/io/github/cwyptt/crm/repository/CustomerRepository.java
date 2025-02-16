package io.github.cwyptt.crm.repository;

import io.github.cwyptt.crm.entity.Customer;
import io.github.cwyptt.crm.utility.constant.QueryConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query(QueryConstants.SEARCH_CUSTOMERS_QUERY)
    List<Customer> searchCustomers(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("company") String company
    );
}
