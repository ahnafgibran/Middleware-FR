package id.grit.facereco.repository;

import java.util.UUID;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import id.grit.facereco.entity.BloodType;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Repository
@CrossOrigin(maxAge = 3600)

public interface BloodTypeRepository extends JpaRepository<BloodType, Integer> {
    Optional<BloodType> findByUuid(UUID uuid);
}
