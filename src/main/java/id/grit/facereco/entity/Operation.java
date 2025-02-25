package id.grit.facereco.entity;

import java.util.Date;
import java.util.UUID;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "operation", schema = "public")
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @Column(name = "operation_code", nullable = false)
    private String operation_code;

    @Column(name = "operation_name", nullable = false)
    private String operation_name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "operation_start_date", nullable = true)
    private LocalDate operation_start_date;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "operation_end_date", nullable = true)
    private LocalDate operation_end_date;

    @Column(name = "operation_total_personnel", nullable = false)
    private Integer operation_total_personnel;

    @Column(name = "operation_patrol_area", nullable = false)
    private String operation_patrol_area;

    @Column(name = "operation_purpose", nullable = false)
    private String operation_purpose;

    @Column(name = "operation_supervisor_name", nullable = false)
    private String operation_supervisor_name;

    @Column(name = "operation_organization_id", nullable = false)
    private UUID operation_organization_id;

    @Column(name = "operation_status_id", nullable = false)
    private UUID operation_status_id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "date_created", nullable = false)
    private Date date_created;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "date_modified", nullable = false)
    private Date date_modified;

    @Column(name = "last_modified_by", nullable = false)
    private UUID last_modified_by;

}
