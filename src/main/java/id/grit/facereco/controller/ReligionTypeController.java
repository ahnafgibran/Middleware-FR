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
import id.grit.facereco.model.request.ReligionTypeRequest;
import id.grit.facereco.services.ReligionTypeServices;

@RestController
@Validated
@CrossOrigin(maxAge = 3600)
@RequestMapping("api/v2/religion-type")
@Tag(name = "Religion Type", description = "")
public class ReligionTypeController {

    private ReligionTypeServices religionTypeServices;

    public ReligionTypeController(ReligionTypeServices religionTypeServices) {
        this.religionTypeServices = religionTypeServices;
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new Religion Type", description = "Endpoint untuk membuat data Religion Type baru")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil Menambahkan Data Religion Type Baru", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Menambahkan Data Religion Type\" }"))),

            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
    })
    public ResponseEntity<LinkedHashMap<String, Object>> createReligionType(
            @RequestBody(required = true) ReligionTypeRequest religionTypeRequest, HttpServletRequest request) {
        return religionTypeServices.serviceCreateData(religionTypeRequest, request);
    }

    @PutMapping("/update/{uuid}")
    @Operation(summary = "Update Religion Type data", description = "Endpoint untuk mengubah data Religion Type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil Mengubah Data Religion Type", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Mengubah Data Religion Type\" }"))),

            @ApiResponse(responseCode = "404", description = "Data Religion Type Tidak Ditemukan", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Data Religion Type Tidak Ditemukan\" }"))),

            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
    })
    public ResponseEntity<LinkedHashMap<String, Object>> updateReligionType(
            @PathVariable UUID uuid, @RequestBody(required = true) ReligionTypeRequest religionTypeRequest,
            HttpServletRequest request) {
        return religionTypeServices.serviceUpdateData(uuid, religionTypeRequest, request);
    }

    @GetMapping("/get-list")
    @Operation(summary = "Get Religion Type List Data", description = "Endpoint untuk mendapatkan list data Religion Type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil Mengambil Data Religion Type", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Mengambil Data Religion Type\", \"data\":[{ \"id\": \"34b3598a-de36-4585-a26a-7db5acf35f7a\", \"religion_type_name\": \"ISLAM\" , \"flag_active_religion_type\":true }]}"))),

            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
    })
    public ResponseEntity<LinkedHashMap<String, Object>> getListReligionType() {
        return religionTypeServices.serviceGetListData();
    }
}
