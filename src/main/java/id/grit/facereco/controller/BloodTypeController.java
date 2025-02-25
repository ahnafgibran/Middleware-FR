package id.grit.facereco.controller;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import id.grit.facereco.model.request.BloodTypeRequest;
import id.grit.facereco.services.BloodTypeServices;

@RestController
@Validated
@CrossOrigin(maxAge = 3600)
@RequestMapping("api/v2/blood-type")
@Tag(name = "Blood Type", description = "")
public class BloodTypeController {

    private BloodTypeServices bloodTypeServices;

    public BloodTypeController(BloodTypeServices bloodTypeServices) {
        this.bloodTypeServices = bloodTypeServices;
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new Blood Type", description = "Endpoint untuk membuat data Blood Type baru")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil Menambahkan Data Blood Type Baru", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Menambahkan Data Blood Type\" }"))),

            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
    })
    public ResponseEntity<LinkedHashMap<String, Object>> createBloodType(
            @RequestBody(required = true) BloodTypeRequest bloodTypeRequest, HttpServletRequest request) {
        return bloodTypeServices.serviceCreateData(bloodTypeRequest, request);
    }

    @PutMapping("/update/{uuid}")
    @Operation(summary = "Update Blood Type data", description = "Endpoint untuk mengubah data Blood Type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil Mengubah Data Blood Type", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Mengubah Data Blood Type\" }"))),

            @ApiResponse(responseCode = "404", description = "Data Blood Type Tidak Ditemukan", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Data Blood Type Tidak Ditemukan\" }"))),

            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
    })
    public ResponseEntity<LinkedHashMap<String, Object>> updateBloodType(
            @PathVariable UUID uuid, @RequestBody(required = true) BloodTypeRequest bloodTypeRequest,
            HttpServletRequest request) {
        return bloodTypeServices.serviceUpdateData(uuid, bloodTypeRequest, request);
    }

    @GetMapping("/get-list")
    @Operation(summary = "Get Blood Type List Data", description = "Endpoint untuk mendapatkan list data Blood Type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil Mengambil Data Blood Type", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Mengambil Data Blood Type\", \"data\":[{ \"id\": \"34b3598a-de36-4585-a26a-7db5acf35f7a\", \"blood_type_name\": \"B\" , \"flag_active_blood_type\":true }]}"))),

            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
    })
    public ResponseEntity<LinkedHashMap<String, Object>> getListBloodType() {
        return bloodTypeServices.serviceGetListData();
    }
}
