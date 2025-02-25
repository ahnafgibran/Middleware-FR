package id.grit.facereco.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import id.grit.facereco.entity.PersonFace;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Repository
public interface PersonFaceRepository extends JpaRepository<PersonFace, Integer> {
    PersonFace findByNik(String nik);
    Optional<PersonFace> findByUuid(UUID uuid);
    boolean existsByNik(String nik);
}
