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
import id.grit.facereco.model.request.NationalityTypeRequest;
import id.grit.facereco.services.NationalityTypeServices;

@RestController
@Validated
@CrossOrigin(maxAge = 3600)
@RequestMapping("api/v2/nationality-type")
@Tag(name = "Nationality Type", description = "")
public class NationalityTypeController {

    private NationalityTypeServices nationalityTypeServices;

    public NationalityTypeController(NationalityTypeServices nationalityTypeServices) {
        this.nationalityTypeServices = nationalityTypeServices;
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new nationality type", description = "Endpoint untuk membuat data nationality type baru")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil Menambahkan Data Nationality Type Baru", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Menambahkan Data Nationality Type\" }"))),

            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
    })
    public ResponseEntity<LinkedHashMap<String, Object>> createNationalityType(
            @RequestBody(required = true) NationalityTypeRequest nationalityTypeRequest, HttpServletRequest request) {
        return nationalityTypeServices.serviceCreateData(nationalityTypeRequest, request);
    }

    @PutMapping("/update/{uuid}")
    @Operation(summary = "Update nationality type data", description = "Endpoint untuk mengubah data nationality type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil Mengubah Data Nationality Type", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Mengubah Data Nationality Type\" }"))),

            @ApiResponse(responseCode = "404", description = "Data Nationality Type Tidak Ditemukan", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Data Nationality Type Tidak Ditemukan\" }"))),

            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
    })
    public ResponseEntity<LinkedHashMap<String, Object>> updateNationalityType(
            @PathVariable UUID uuid, @RequestBody(required = true) NationalityTypeRequest nationalityTypeRequest,
            HttpServletRequest request) {
        return nationalityTypeServices.serviceUpdateData(uuid, nationalityTypeRequest, request);
    }

    @GetMapping("/get-list")
    @Operation(summary = "Get Nationality Type List Data", description = "Endpoint untuk mendapatkan list data nationality type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil Mengambil Data Nationality Type", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Mengambil Data Nationality Type\", \"data\":[{ \"id\": \"34b3598a-de36-4585-a26a-7db5acf35f7a\", \"nationality_type_name\": \"Warga Negara Indonesia\" , \"flag_active_nationality_type\":true }]}"))),

            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
    })
    public ResponseEntity<LinkedHashMap<String, Object>> getListNationalityType() {
        return nationalityTypeServices.serviceGetListData();
    }
}
