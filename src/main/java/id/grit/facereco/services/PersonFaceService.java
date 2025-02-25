// Service Class
package id.grit.facereco.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import id.grit.facereco.config.JwtUtil;
import id.grit.facereco.entity.BloodType;
import id.grit.facereco.entity.MaritalType;
import id.grit.facereco.entity.NationalityType;
import id.grit.facereco.entity.PersonFace;
import id.grit.facereco.entity.ReligionType;
import id.grit.facereco.model.request.PersonEngineReq;
import id.grit.facereco.model.response.FaceSimilarityRes;
import id.grit.facereco.model.response.PersonEngineRes;
import id.grit.facereco.repository.BloodTypeRepository;
import id.grit.facereco.repository.GenderRepository;
import id.grit.facereco.repository.MaritalTypeRepository;
import id.grit.facereco.repository.NationalityTypeRepository;
import id.grit.facereco.repository.PersonFaceRepository;
import id.grit.facereco.repository.ReligionTypeRepository;
import io.infran.driverinfranfaceid.IdentifyOneResponse;
import io.infran.driverinfranfaceid.RegisterUserResponse;
import jakarta.servlet.http.HttpServletRequest;


@Service
public class PersonFaceService {

    @Autowired
    private final JwtUtil jwtUtil = null;

    @Autowired
    private PersonFaceRepository personFaceRepository;

    @Autowired
    private FaceRecService faceRecService;

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private BloodTypeRepository bloodTypeRepository;

    @Autowired
    private ReligionTypeRepository religionTypeRepository;

    @Autowired
    private MaritalTypeRepository maritalTypeRepository;

    @Autowired
    private NationalityTypeRepository nationalityTypeRepository;

    

    public ResponseEntity<LinkedHashMap<String, Object>> registerFaceFromJson(byte[] imageBytes, PersonEngineReq facePerson, HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            String userUuidFromToken = jwtUtil.getUuidFromToken(request);
    
            if (imageBytes == null || imageBytes.length == 0) {
                response.put("status", 400);
                response.put("message", "Image cannot be null or empty.");
                response.put("value", null);
                return ResponseEntity.badRequest().body(response);
            }
    
            // Validasi panjang NIK
            if (facePerson.getNik() == null || facePerson.getNik().length() != 16) {
                response.put("status", 400);
                response.put("message", "NIK must be exactly 16 characters long.");
                response.put("value", null);
                return ResponseEntity.badRequest().body(response);
            }
    
            if (personFaceRepository.existsByNik(facePerson.getNik())) {
                response.put("status", 400);
                response.put("message", "NIK already exists.");
                response.put("value", null);
                return ResponseEntity.badRequest().body(response);
            }
    
            byte[] resizedBytes = ImageUtilService.resizeByteImage(imageBytes);
    
            RegisterUserResponse registerUserResponse = faceRecService.registerFace("User registration", resizedBytes).get();
            System.out.println("RegisterUserResponse : " + registerUserResponse);
    
            // if (!"0".equals(registerUserResponse.getErrCode())) {
            //     response.put("status", 404);
            //     response.put("message", registerUserResponse.getMessage() + ", " + registerUserResponse.getResult());
            //     response.put("value", null);
            //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            // }
    
            UUID registeredUUID = UUID.fromString(registerUserResponse.getUserID());
            System.out.println("UUID ==========> " + registeredUUID);
    
            // Fetch related entities using UUID
            id.grit.facereco.entity.Gender gender = genderRepository.findByUuid(facePerson.getGenderId())
                    .orElseThrow(() -> new RuntimeException("Gender not found"));
            BloodType bloodType = bloodTypeRepository.findByUuid(facePerson.getBloodTypeId())
                    .orElseThrow(() -> new RuntimeException("Blood type not found"));
            ReligionType religion = religionTypeRepository.findByUuid(facePerson.getReligionId())
                    .orElseThrow(() -> new RuntimeException("Religion not found"));
            MaritalType maritalType = maritalTypeRepository.findByUuid(facePerson.getMaritalTypeId())
                    .orElseThrow(() -> new RuntimeException("Marital type not found"));
            NationalityType nationality = nationalityTypeRepository.findByUuid(facePerson.getNationalityId())
                    .orElseThrow(() -> new RuntimeException("Nationality not found"));
    
            // Save new person
            PersonFace newPerson = new PersonFace();
            newPerson.setUuid(registeredUUID);
            newPerson.setNik(facePerson.getNik());
            newPerson.setFullName(facePerson.getFullName());
            newPerson.setPlaceOfBirth(facePerson.getPlaceOfBirth());
            newPerson.setDateOfBirth(facePerson.getDateOfBirth());
            newPerson.setAddress(facePerson.getAddress());
            newPerson.setOccupation(facePerson.getOccupation());
            newPerson.setGender(gender);
            newPerson.setBloodType(bloodType);
            newPerson.setReligion(religion);
            newPerson.setMaritalType(maritalType);
            newPerson.setNationality(nationality);
            newPerson.setDpo(false);
            newPerson.setImage(resizedBytes);
            newPerson.setLast_modified_by(UUID.fromString(userUuidFromToken));
    
            personFaceRepository.save(newPerson);
    
            response.put("status", 200);
            response.put("message", "User successfully registered.");
            return ResponseEntity.ok(response);
    
        } catch (IOException | InterruptedException | ExecutionException e) {
            response.put("status", 500);
            response.put("message", e.getMessage());
            response.put("value", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    

    public ResponseEntity<Object> identifyOne(MultipartFile file, int fetchLimit) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
    
        try {
            // Validasi input file
            validateFile(file);
            byte[] resizedBytes = ImageUtilService.resizeFileImage(file);
    
            // Panggil layanan identifikasi wajah
            IdentifyOneResponse identifyOneResp = faceRecService.identifyOne(resizedBytes).get();
            System.out.println("IdentifyOneResponse : " + identifyOneResp);
    
            // Validasi respons dari FaceRecService
            if (!"0".equals(identifyOneResp.getErrCode())) {
                response.put("status", 500);
                response.put("message", identifyOneResp.getMessage() + ", " + identifyOneResp.getResult());
                return ResponseEntity.status(500).body(response);
            }
    
            String userUUID = identifyOneResp.getMatchEmbeddingID();
            UUID uuid = UUID.fromString(userUUID);
    
            // Gunakan findByUuid
            Optional<PersonFace> personOpt = personFaceRepository.findByUuid(uuid);
    
            if (personOpt.isEmpty()) {
                response.put("status", 200);
                response.put("message", "Person not found.");
                return ResponseEntity.status(200).body(response);
            }
    
            PersonFace person = personOpt.get();
            float confidenceScore = identifyOneResp.getConfidenceScore();
    
            FaceSimilarityRes similarityRes = new FaceSimilarityRes();
            similarityRes.setSimilarity(confidenceScore);
            similarityRes.setPersonDetail(person.toPersonShort());
    
            if (confidenceScore < 0.55) {
                similarityRes.getPersonDetail().setImage(null); 
            }
    
            List<FaceSimilarityRes> rank = new ArrayList<>();
            rank.add(similarityRes);
    
            return ResponseEntity.ok(rank);
    
        } catch (IOException e) {
            response.put("status", 500);
            response.put("message", "IO error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            response.put("status", 500);
            response.put("message", "Operation interrupted.");
            return ResponseEntity.status(500).body(response);
        } catch (ExecutionException e) {
            response.put("status", 500);
            response.put("message", "Execution error: " + e.getCause().getMessage());
            return ResponseEntity.status(500).body(response);
        } catch (IllegalArgumentException e) {
            response.put("status", 400);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } 
    }
    
   public ResponseEntity<List<PersonEngineRes>> getAllPersonFaces() {
    try {
        List<PersonFace> personFaces = personFaceRepository.findAll();
        List<PersonEngineRes> responseList = personFaces.stream().map(personFace -> {
            PersonEngineRes response = new PersonEngineRes();
            response.setId(personFace.getId());
            response.setUuid(personFace.getUuid());
            response.setNik(personFace.getNik());
            response.setFullName(personFace.getFullName());
            response.setGenderName(personFace.getGender().getGender_name());
            response.setPlaceOfBirth(personFace.getPlaceOfBirth());
            response.setDateOfBirth(personFace.getDateOfBirth());
            response.setBloodTypeName(personFace.getBloodType().getBlood_type_name());
            response.setAddress(personFace.getAddress());
            response.setReligionName(personFace.getReligion().getReligion_type_name());
            response.setMaritalStatus(personFace.getMaritalType().getMarital_type_name());
            response.setOccupation(personFace.getOccupation());
            response.setNationalityName(personFace.getNationality().getNationality_type_name());
            response.setDpo(personFace.isDpo());
            response.setImage(personFace.getImage());
            response.setDateCreated(personFace.getDate_created());
            response.setDateModified(personFace.getDate_modified());
            response.setLastModifiedBy(personFace.getLast_modified_by());
            return response;
        }).toList();

        return ResponseEntity.ok(responseList);
    } catch (Exception e) {
        return ResponseEntity.status(500).body(null);
    }
}

public ResponseEntity<LinkedHashMap<String, Object>> deletePersonFace(UUID uuid) {
    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
    try {
        // Cari entri berdasarkan UUID
        Optional<PersonFace> personOpt = personFaceRepository.findByUuid(uuid);

        if (personOpt.isEmpty()) {
            response.put("status", 404);
            response.put("message", "Person with UUID " + uuid + " not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        personFaceRepository.delete(personOpt.get());

        response.put("status", 200);
        response.put("message", "Person with UUID " + uuid + " successfully deleted.");
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        response.put("status", 500);
        response.put("message", "Error occurred: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}



    
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("The file can't be empty!!");
        }
        if (!file.getContentType().contains("image") && !file.getContentType().contains("octet-stream")) {
            throw new IllegalArgumentException("The file type must be image!");
        }
    }
}

