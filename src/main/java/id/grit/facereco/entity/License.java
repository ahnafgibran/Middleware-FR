package id.grit.facereco.entity;

import java.time.LocalDate;
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

@Entity
@Data
@Table(name = "license", schema = "public")
public class License {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @Column(name = "license_name", nullable = false)
    private String license_name;

    @Column(name = "license_description", nullable = false)
    private String license_description;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "license_activated_date", nullable = false)
    private Date license_activated_date;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "license_expired_date", nullable = true)
    private LocalDate license_expired_date;

    @Column(name = "license_serial_number", nullable = false)
    private String license_serial_number;

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
