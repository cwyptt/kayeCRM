package io.github.cwyptt.crm.repository;

import io.github.cwyptt.crm.entity.Company;
import io.github.cwyptt.crm.utility.constant.QueryConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByEmail(String email);
    Optional<Company> findByEmail(String email);

    @Query(QueryConstants.SEARCH_COMPANIES_QUERY)
    List<Company> searchCompanies(
            @Param("name") String name,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("industry") String industry
    );
}
