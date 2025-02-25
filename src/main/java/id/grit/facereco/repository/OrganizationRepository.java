package id.grit.facereco.repository;

import java.util.UUID;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import id.grit.facereco.entity.Organization;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Repository
@CrossOrigin(maxAge = 3600)

public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
    Optional<Organization> findByUuid(UUID uuid);

    @Query(nativeQuery = true, value = "SELECT org.* FROM organization org"
            + " WHERE (lower(org.organization_name) LIKE lower(concat('%', :keywords ,'%'))"
            + " OR lower(org.organization_code) LIKE lower(concat('%', :keywords ,'%'))"
            + " OR lower(org.organization_office_address) LIKE lower(concat('%', :keywords ,'%'))"
            + " OR lower(org.organization_office_telephone) LIKE lower(concat('%', :keywords ,'%'))"
            + " OR lower(org.organization_office_email) LIKE lower(concat('%', :keywords ,'%'))"
            + " )"
            + " ORDER BY org.id desc")
    Page<Organization> findByOrganizationDataList(@Param("keywords") String keywords, Pageable pageable);
}
