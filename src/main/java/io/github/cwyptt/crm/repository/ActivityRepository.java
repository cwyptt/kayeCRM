package io.github.cwyptt.crm.repository;

import io.github.cwyptt.crm.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findTop20ByOrderByTimestampDesc();
}
