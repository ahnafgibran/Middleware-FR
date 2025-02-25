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
@Table(name = "organization", schema = "public")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @Column(name = "organization_photo", nullable = false)
    private String organization_photo;

    @Column(name = "organization_name", nullable = false)
    private String organization_name;

    @Column(name = "organization_code", nullable = false)
    private String organization_code;

    @Column(name = "organization_office_address", nullable = false)
    private String organization_office_address;

    @Column(name = "organization_office_telephone", nullable = false)
    private String organization_office_telephone;

    @Column(name = "organization_office_email", nullable = false)
    private String organization_office_email;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "organization_date_establishment", nullable = true)
    private LocalDate organization_date_establishment;

    @Column(name = "organization_jurisdiction_level_id", nullable = false)
    private UUID organization_jurisdiction_level_id;

    @Column(name = "organization_total_personnel", nullable = false)
    private Integer organization_total_personnel;

    @Column(name = "organization_parent_id", nullable = true)
    private UUID organization_parent_id;

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
