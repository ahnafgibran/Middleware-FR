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
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "streaming_device", schema = "public")
public class StreamingDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @Column(name = "device_name", nullable = false)
    private String device_name;

    @Column(name = "ip_address", nullable = false)
    private String ip_address;

    @Column(name = "device_url_rtsp", nullable = false)
    private String device_url_rtsp;

    @Column(name = "latitude_device", nullable = true)
    private String latitude_device;

    @Column(name = "longitude_device", nullable = true)
    private String longitude_device;

    @Column(name = "device_organization_id", nullable = false)
    private UUID device_organization_id;

    @Column(name = "device_type_id", nullable = false)
    private UUID device_type_id;

    @Column(name = "device_operation_id", nullable = true)
    private UUID device_operation_id;

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
