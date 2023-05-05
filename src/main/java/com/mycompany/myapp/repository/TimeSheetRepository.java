package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TimeSheet;
import com.mycompany.myapp.service.dto.TimeSheetDTO;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TimeSheet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeSheetRepository extends JpaRepository<TimeSheet, Long> {
    Optional<TimeSheet> findByDate(LocalDate date);
}
