package id.grit.facereco.controller;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import id.grit.facereco.model.request.PersonEngineReq;
import id.grit.facereco.model.response.PersonEngineRes;
import id.grit.facereco.services.PersonFaceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v2/person")
public class PersonFaceController {

    @Autowired
    private PersonFaceService personFaceService;

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<LinkedHashMap<String, Object>> registerPerson(
            @RequestBody PersonEngineReq facePerson,
            HttpServletRequest request) {

        if (facePerson.getImage() == null || facePerson.getImage().isEmpty()) {
            LinkedHashMap<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("status", 400);
            errorResponse.put("message", "Image is missing or empty.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        byte[] imageBytes;
        try {
            imageBytes = Base64.getDecoder().decode(facePerson.getImage());
        } catch (IllegalArgumentException e) {
            LinkedHashMap<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("status", 400);
            errorResponse.put("message", "Invalid Base64 image format.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        return personFaceService.registerFaceFromJson(imageBytes, facePerson, request);
    }

    

    @PostMapping(value = "/identify-one", consumes = { "multipart/form-data" })
    public ResponseEntity<Object> identifyOne(
            @RequestParam("file") @NotNull MultipartFile file,
            @RequestParam("fetch_limit") @Min(1) @Max(10) Integer fetchLimit) {

        return personFaceService.identifyOne(file, fetchLimit);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PersonEngineRes>> getAllPersonFaces() {
    return personFaceService.getAllPersonFaces();
    }

     @DeleteMapping("/{uuid}")
        public ResponseEntity<LinkedHashMap<String, Object>> deletePersonFace(@PathVariable UUID uuid) {
        return personFaceService.deletePersonFace(uuid);
    }

}
