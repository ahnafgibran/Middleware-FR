package id.grit.facereco.entity;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import id.grit.facereco.model.response.PersonEngineRes;
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
@Table(name = "person_fr", schema = "public")
public class PersonFace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "nik", nullable = false, unique = true, length=16)
    private String nik;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @ManyToOne
    @JoinColumn(name = "gender_id", nullable = false)
    private Gender gender;

    @Column(name = "place_of_birth", nullable = false)
    private String placeOfBirth;

    @Column(name = "date_of_birth", nullable = false)
    private Date dateOfBirth; 

    @ManyToOne
    @JoinColumn(name = "blood_type_id", nullable = false)
    private BloodType bloodType;

    @Column(name = "address", nullable = false)
    private String address;

    @ManyToOne
    @JoinColumn(name = "religion_type_id", nullable = false)
    private ReligionType religion;

    @ManyToOne
    @JoinColumn(name = "marital_type_id", nullable = false)
    private MaritalType maritalType;

    @Column(name = "occupation", nullable = false)
    private String occupation;

    @ManyToOne
    @JoinColumn(name = "nationality_type_id", nullable = false)
    private NationalityType nationality;

    @Column(name = "is_dpo", nullable = false)
    private boolean isDpo;

    @Column(name = "image", nullable = true)
    private byte[] image;

    @Column(name = "is_active", nullable = true)
    private boolean is_active;

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

     public PersonEngineRes toPersonShort() {
        PersonEngineRes personEngineRes = new PersonEngineRes();
        personEngineRes.setId(this.getId());
        personEngineRes.setUuid(this.getUuid());
        personEngineRes.setNik(this.getNik());
        personEngineRes.setFullName(this.getFullName());
        personEngineRes.setGenderName(this.getGender() != null ? this.getGender().getGender_name() : null);
        personEngineRes.setPlaceOfBirth(this.getPlaceOfBirth());
        personEngineRes.setDateOfBirth(this.getDateOfBirth());
        personEngineRes.setBloodTypeName(this.getBloodType() != null ? this.getBloodType().getBlood_type_name() : null);
        personEngineRes.setAddress(this.getAddress());
        personEngineRes.setReligionName(this.getReligion() != null ? this.getReligion().getReligion_type_name() : null);
        personEngineRes.setMaritalStatus(this.getMaritalType() != null ? this.getMaritalType().getMarital_type_name() : null);
        personEngineRes.setOccupation(this.getOccupation());
        personEngineRes.setNationalityName(this.getNationality() != null ? this.getNationality().getNationality_type_name() : null);
        personEngineRes.setDpo(this.isDpo());
        personEngineRes.setImage(this.getImage());
        personEngineRes.setDateCreated(this.getDate_created());
        personEngineRes.setDateModified(this.getDate_modified());
        personEngineRes.setLastModifiedBy(this.getLast_modified_by());
        return personEngineRes;
    }

}
