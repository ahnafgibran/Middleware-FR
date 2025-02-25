package id.grit.facereco.repository;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import id.grit.facereco.entity.License;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Repository
@CrossOrigin(maxAge = 3600)
public interface LicenseRepository extends JpaRepository<License, Integer> {
    Optional<License> findByUuid(UUID uuid);

    boolean deleteByUuid(UUID uuid);

    @Query(nativeQuery = true, value = "SELECT lcs.* FROM license lcs"
            + " WHERE (lower(lcs.license_name) LIKE lower(concat('%', :keywords ,'%'))"
            + " OR lower(lcs.license_serial_number) LIKE lower(concat('%', :keywords ,'%'))"
            + " )"
            + " ORDER BY lcs.id desc")
    Page<License> findByLicenseDataList(@Param("keywords") String keywords, Pageable pageable);
}
