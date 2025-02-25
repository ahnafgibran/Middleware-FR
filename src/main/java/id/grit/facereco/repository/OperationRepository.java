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

import id.grit.facereco.entity.Operation;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Repository
@CrossOrigin(maxAge = 3600)
public interface OperationRepository extends JpaRepository<Operation, Integer> {
        Optional<Operation> findByUuid(UUID uuid);

        // Ini untuk search data by keyword dengan staging berdasarkan organisasi ID nya
        // (Biasanya untuk organisasi yang memiliki parent organisasi) contoh POLDA BALI
        @Query(nativeQuery = true, value = "SELECT opr.* FROM operation opr"
                        + " WHERE (lower(opr.operation_code) LIKE lower(concat('%', :keywords ,'%'))"
                        + " OR lower(opr.operation_name) LIKE lower(concat('%', :keywords ,'%')))"
                        + " AND opr.operation_organization_id = :organizationId ORDER BY opr.id desc")
        Page<Operation> findDataByKeywordAndOrgId(@Param("keywords") String keywords,
                        @Param("organizationId") UUID organizationId, Pageable pageable);

        // Ini untuk search data by keyword tanpa ada staging organisasi ID nya
        // (Biasanya untuk organisasi yang tidak memiliki parent organisasi)
        // contoh POLRI
        @Query(nativeQuery = true, value = "SELECT opr.* FROM operation opr"
                        + " WHERE (lower(opr.operation_code) LIKE lower(concat('%', :keywords ,'%'))"
                        + " OR lower(opr.operation_name) LIKE lower(concat('%', :keywords ,'%')))"
                        + " ORDER BY opr.id desc")
        Page<Operation> findDataByKeyword(@Param("keywords") String keywords, Pageable pageable);

        // Ini untuk getAllData dengan staging organisasi ID nya
        // (Biasanya untuk organisasi yang memiliki parent organisasi) contoh POLDA BALI
        @Query(nativeQuery = true, value = "SELECT opr.* FROM operation opr"
                        + " WHERE opr.operation_organization_id = :organizationId "
                        + " ORDER BY opr.id desc")
        Page<Operation> findAllDataByOrgId(@Param("organizationId") UUID organizationId, Pageable pageable);

        // Ini untuk getAllData tanpa staging organisasi ID nya
        // (Biasanya untuk organisasi yang tidak memiliki parent organisasi)
        // contoh POLRI
        @Query(nativeQuery = true, value = "SELECT opr.* FROM operation opr"
                        + " ORDER BY opr.id desc")
        Page<Operation> findAllData(Pageable pageable);
}
