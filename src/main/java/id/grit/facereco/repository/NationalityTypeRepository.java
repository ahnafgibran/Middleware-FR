package id.grit.facereco.repository;

import java.util.UUID;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import id.grit.facereco.entity.NationalityType;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Repository
@CrossOrigin(maxAge = 3600)

public interface NationalityTypeRepository extends JpaRepository<NationalityType, Integer> {
    Optional<NationalityType> findByUuid(UUID uuid);
}
