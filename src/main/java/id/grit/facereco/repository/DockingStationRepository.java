package id.grit.facereco.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import id.grit.facereco.entity.DockingEntity;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Repository
@CrossOrigin(maxAge = 3600)
public interface DockingStationRepository extends JpaRepository<DockingEntity, Integer> {

    Optional<DockingEntity> findByUuid(UUID uuid);

    // Ini untuk search data by keyword dengan staging berdasarkan organisasi ID nya
    // (Biasanya untuk organisasi yang memiliki parent organisasi) contoh POLDA BALI
    @Query(nativeQuery = true, value = "SELECT dock.* FROM docking_station dock"
            + " WHERE (lower(dock.docking_station_name) LIKE lower(concat('%', :keywords ,'%'))"
            + " OR lower(dock.docking_station_ip_address) LIKE lower(concat('%', :keywords ,'%')))"
            + " AND dock.docking_station_organization_id = :organizationId ORDER BY dock.id desc")
    Page<DockingEntity> findDataByKeywordAndOrgId(@Param("keywords") String keywords,
            @Param("organizationId") UUID organizationId, Pageable pageable);

    // Ini untuk search data by keyword tanpa ada staging organisasi ID nya
    // (Biasanya untuk organisasi yang tidak memiliki parent organisasi)
    // contoh POLRI
    @Query(nativeQuery = true, value = "SELECT dock.* FROM docking_station dock"
            + " WHERE (lower(dock.docking_station_name) LIKE lower(concat('%', :keywords ,'%'))"
            + " OR lower(dock.docking_station_ip_address) LIKE lower(concat('%', :keywords ,'%')))"
            + " ORDER BY dock.id desc")
    Page<DockingEntity> findDataByKeyword(@Param("keywords") String keywords, Pageable pageable);

    // Ini untuk getAllData dengan staging organisasi ID nya
    // (Biasanya untuk organisasi yang memiliki parent organisasi) contoh POLDA BALI
    @Query(nativeQuery = true, value = "SELECT dock.* FROM docking_station dock"
            + " WHERE dock.docking_station_organization_id = :organizationId "
            + " ORDER BY dock.id desc")
    Page<DockingEntity> findAllDataByOrgId(@Param("organizationId") UUID organizationId, Pageable pageable);

    // Ini untuk getAllData tanpa staging organisasi ID nya
    // (Biasanya untuk organisasi yang tidak memiliki parent organisasi)
    // contoh POLRI
    @Query(nativeQuery = true, value = "SELECT dock.* FROM docking_station dock"
            + " ORDER BY dock.id desc")
    Page<DockingEntity> findAllData(Pageable pageable);
}
