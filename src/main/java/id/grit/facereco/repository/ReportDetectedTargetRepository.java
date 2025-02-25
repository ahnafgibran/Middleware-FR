package id.grit.facereco.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import id.grit.facereco.entity.ReportDetectedTarget;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Repository
@CrossOrigin(maxAge = 3600)

public interface  ReportDetectedTargetRepository extends JpaRepository<ReportDetectedTarget, Integer> {
    Optional<ReportDetectedTarget> findByUuid(UUID uuid);
}
