package id.grit.facereco.entity;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "user", schema = "public")
public class AuthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "user_full_name", nullable = false)
    private String user_full_name;

    @NotBlank
    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "user_phone", nullable = false)
    private String user_phone;

    @Column(name = "user_password", nullable = false)
    private String user_password;

    @Column(name = "is_active", nullable = false)
    private boolean is_active;

    @ManyToOne
    @JoinColumn(name = "user_gender_id", nullable = false)
    private Gender user_gender_id;

    @ManyToOne
    @JoinColumn(name = "user_organization_id", nullable = false)
    private Organization user_organization_id;

    @ManyToOne
    @JoinColumn(name = "user_permission_id", nullable = false)
    private PermissionGroup user_permission_id;

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
