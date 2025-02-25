package id.grit.facereco.model.response;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

@Data
public class PersonEngineRes {

    private Integer id;
    private UUID uuid;
    private String nik;
    private String fullName;
    private String genderName;       
    private String placeOfBirth;
    private Date dateOfBirth;
    private String bloodTypeName;   
    private String address;
    private String religionName;    
    private String maritalStatus;   
    private String occupation;
    private String nationalityName; 
    @JsonProperty("isDpo") 
    private boolean isDpo;
    private byte[] image;           
    private Date dateCreated;
    private Date dateModified;
    private UUID lastModifiedBy;
}
