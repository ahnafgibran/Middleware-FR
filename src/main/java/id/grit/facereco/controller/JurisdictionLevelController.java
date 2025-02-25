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
import id.grit.facereco.model.request.JurisdictionLevelRequest;
import id.grit.facereco.services.JurisdictionLevelServices;

@RestController
@Validated
@CrossOrigin(maxAge = 3600)
@RequestMapping("api/v2/jurisdiction-level")
@Tag(name = "Jurisdiction Level", description = "")
public class JurisdictionLevelController {

    private JurisdictionLevelServices jurisdictionLevelServices;

    public JurisdictionLevelController(JurisdictionLevelServices jurisdictionLevelServices) {
        this.jurisdictionLevelServices = jurisdictionLevelServices;
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new jurisdiction level", description = "Endpoint untuk membuat data jurisdiction level baru")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Beehasil Menambahkan Data Jurisdiction Level Baru", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Menambahkan Data Jurisdiction Level\" }"))),

            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
    })
    public ResponseEntity<LinkedHashMap<String, Object>> createJurisdictionLevel(
            @RequestBody(required = true) JurisdictionLevelRequest jurisdictionLevelRequest,
            HttpServletRequest request) {
        return jurisdictionLevelServices.serviceCreateData(jurisdictionLevelRequest, request);
    }

    @PutMapping("/update/{uuid}")
    @Operation(summary = "Update jurisdiction level data", description = "Endpoint untuk mengubah data jurisdiction level")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil Mengubah Data Jurisdiction Level", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Mengubah Data Jurisdiction Level\" }"))),

            @ApiResponse(responseCode = "404", description = "Data Jurisdiction Level Tidak Ditemukan", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Data Jurisdiction Level Tidak Ditemukan\" }"))),

            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
    })
    public ResponseEntity<LinkedHashMap<String, Object>> updateJurisdictionLevel(
            @PathVariable UUID uuid,
            @RequestBody JurisdictionLevelRequest jurisdictionLevelRequest, HttpServletRequest request) {
        return jurisdictionLevelServices.serviceUpdateData(uuid, jurisdictionLevelRequest, request);
    }

    @GetMapping("/get-list")
    @Operation(summary = "Get Jurisdiction List Data", description = "Endpoint untuk mendapatkan list data jurisdiction level")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil Mengambil Data Jurisdiction Level", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Mengambil Data Jurisdiction Level\", \"data\":[{ \"id\": \"34b3598a-de36-4585-a26a-7db5acf35f7a\", \"jurisdiction_name\": \"Nasional\" , \"jurisdiction_code\": \"N\", \"flag_active_jurisdiction\":true }]}"))),

            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
    })
    public ResponseEntity<LinkedHashMap<String, Object>> getListJurisdictionLevel() {
        return jurisdictionLevelServices.serviceGetListData();
    }

}
