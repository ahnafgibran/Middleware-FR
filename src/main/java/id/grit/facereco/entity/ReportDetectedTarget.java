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
import lombok.Data;

@Entity
@Data
@Table(name = "report_detected_target", schema="public")
public class ReportDetectedTarget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "date_detected", nullable = false)
    private Date date_detected;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private Report report_id;

    @ManyToOne
    @JoinColumn(name = "report_streaming_device_id", nullable = false)
    private StreamingDevice report_streaming_device_id;

    @ManyToOne
    @JoinColumn(name = "identity_template_id", nullable = false)
    private PersonFace identity_template_id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "date_modified", nullable = false)
    private Date date_modified;

    @Column(name = "last_modified_by", nullable = false)
    private UUID last_modified_by;
}
