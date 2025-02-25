package id.grit.facereco.model.request;

import java.util.Date;
import java.util.UUID;

import lombok.Data;

@Data
public class PersonEngineReq {
    private String nik;
    private String fullName;
    private String placeOfBirth;
    private Date dateOfBirth;
    private String address;
    private String occupation;
    private UUID genderId;
    private UUID bloodTypeId;
    private UUID religionId;
    private UUID maritalTypeId;
    private UUID nationalityId;
    private String image;
}
