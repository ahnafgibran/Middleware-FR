package id.grit.facereco.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import id.grit.facereco.entity.AuthEntity;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public interface AuthRepository extends JpaRepository<AuthEntity, Integer> {
    Optional<AuthEntity> findByEmail(String email);

    Optional<AuthEntity> findByUuid(UUID uuid);
}
