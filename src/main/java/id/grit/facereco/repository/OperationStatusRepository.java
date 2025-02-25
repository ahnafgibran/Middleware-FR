package id.grit.facereco.repository;

import java.util.UUID;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import io.swagger.v3.oas.annotations.Hidden;

import id.grit.facereco.entity.OperationStatus;

@Hidden
@Repository
@CrossOrigin(maxAge = 3600)

public interface OperationStatusRepository extends JpaRepository<OperationStatus, Integer> {
    Optional<OperationStatus> findByUuid(UUID uuid);
}
