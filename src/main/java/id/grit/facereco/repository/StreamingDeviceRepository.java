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

import id.grit.facereco.entity.StreamingDevice;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Repository
@CrossOrigin(maxAge = 3600)
public interface StreamingDeviceRepository extends JpaRepository<StreamingDevice, Integer> {
        Optional<StreamingDevice> findByUuid(UUID uuid);

        // Ini untuk search data by keyword dengan staging berdasarkan organisasi ID nya
        // (Biasanya untuk organisasi yang memiliki parent organisasi) contoh POLDA BALI
        @Query(nativeQuery = true, value = "SELECT strm.* FROM streaming_device strm"
                        + " WHERE (lower(strm.device_name) LIKE lower(concat('%', :keywords ,'%'))"
                        + " OR lower(strm.ip_address) LIKE lower(concat('%', :keywords ,'%'))"
                        + " OR lower(strm.device_url_rtsp) LIKE lower(concat('%', :keywords ,'%')))"
                        + " AND strm.device_organization_id = :organizationId ORDER BY strm.id desc")
        Page<StreamingDevice> findDataByKeywordAndOrgId(@Param("keywords") String keywords,
                        @Param("organizationId") UUID organizationId, Pageable pageable);

        // Ini untuk search data by keyword tanpa ada staging organisasi ID nya
        // (Biasanya untuk organisasi yang tidak memiliki parent organisasi)
        // contoh POLRI
        @Query(nativeQuery = true, value = "SELECT strm.* FROM streaming_device strm"
                        + " WHERE (lower(strm.device_name) LIKE lower(concat('%', :keywords ,'%'))"
                        + " OR lower(strm.ip_address) LIKE lower(concat('%', :keywords ,'%'))"
                        + " OR lower(strm.device_url_rtsp) LIKE lower(concat('%', :keywords ,'%')))"
                        + " ORDER BY strm.id desc")
        Page<StreamingDevice> findDataByKeyword(@Param("keywords") String keywords, Pageable pageable);

        // Ini untuk getAllData dengan staging organisasi ID nya
        // (Biasanya untuk organisasi yang memiliki parent organisasi) contoh POLDA BALI
        @Query(nativeQuery = true, value = "SELECT strm.* FROM streaming_device strm"
                        + " WHERE strm.device_organization_id = :organizationId "
                        + " ORDER BY strm.id desc")
        Page<StreamingDevice> findAllDataByOrgId(@Param("organizationId") UUID organizationId, Pageable pageable);

        // Ini untuk getAllData tanpa staging organisasi ID nya
        // (Biasanya untuk organisasi yang tidak memiliki parent organisasi)
        // contoh POLRI
        @Query(nativeQuery = true, value = "SELECT strm.* FROM streaming_device strm"
                        + " ORDER BY strm.id desc")
        Page<StreamingDevice> findAllData(Pageable pageable);

}
