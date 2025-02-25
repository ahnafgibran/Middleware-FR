package id.grit.facereco.controller;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import id.grit.facereco.model.request.OrganizationRequest;
import id.grit.facereco.services.OrganizationServices;

@RestController
@Validated
@CrossOrigin(maxAge = 3600)
@RequestMapping("api/v2/organization")
@Tag(name = "Police Organization", description = "")
public class OrganizationController {

        private OrganizationServices organizationServices;

        public OrganizationController(OrganizationServices organizationServices) {
                this.organizationServices = organizationServices;
        }

        @PostMapping("/create")
        @Operation(summary = "Create a new organization", description = "Endpoint untuk membuat data organization baru")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Berhasil Menambahkan Data Organization Baru", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Menambahkan Data Organization\" }"))),

                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
        })
        public ResponseEntity<LinkedHashMap<String, Object>> createOrganization(
                        @RequestBody(required = true) OrganizationRequest organizationRequest,
                        HttpServletRequest request) {
                return organizationServices.serviceCreateData(organizationRequest, request);
        }

        @PutMapping("/update/{uuid}")
        @Operation(summary = "Update organization data", description = "Endpoint untuk mengubah data organization")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Berhasil Mengubah Data Organization", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Mengubah Data Organization\" }"))),

                        @ApiResponse(responseCode = "404", description = "Data Organization Tidak Ditemukan", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Data Organization Tidak Ditemukan\" }"))),

                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
        })
        public ResponseEntity<LinkedHashMap<String, Object>> updateOrganization(@PathVariable UUID uuid,
                        @RequestBody(required = true) OrganizationRequest organizationRequest,
                        HttpServletRequest request) {
                return organizationServices.serviceUpdateData(uuid, organizationRequest, request);
        }

        @DeleteMapping("/delete/{uuid}")
        @Operation(summary = "Delete Organization Data", description = "Endpoint untuk menghapus data organization")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Berhasil Menghapus Data Organization", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Menghapus Data Organization\" }"))),

                        @ApiResponse(responseCode = "404", description = "Data Organization Tidak Ditemukan", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Data Organization Tidak Ditemukan\" }"))),

                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

                        @ApiResponse(responseCode = "409", description = "Conflict Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Gagal menghapus data, karena data ini masih digunakan untuk referensi data lain\" }"))),

                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
        })
        public ResponseEntity<LinkedHashMap<String, Object>> deleteOrganization(@PathVariable UUID uuid) {
                return organizationServices.serviceDeleteData(uuid);
        }

        @GetMapping("/get-list")
        @Operation(summary = "Get Organization List Data", description = "Endpoint untuk mendapatkan list data organization")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Berhasil Mengambil Data Organization", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{\n"
                                        + //
                                        "  \"status\": \"SUKSES\",\n" + //
                                        "  \"message\": \"Berhasil Mengambil Data Organization\",\n" + //
                                        "  \"data\": [\n" + //
                                        "    {\n" + //
                                        "      \"id\": \"dd61d9aa-9fd6-462a-98a8-a2a8338ded3f\",\n" + //
                                        "      \"organization_photo\": \"/path/image/logo_polri\",\n" + //
                                        "      \"organization_name\": \"POLRI\",\n" + //
                                        "      \"organization_code\": \"POLRI\",\n" + //
                                        "      \"organization_office_address\": \"Jl. Trunojoyo No.3 2, RT.2/RW.1, Selong, Kec. Kby. Baru, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta 12110\",\n"
                                        + //
                                        "      \"organization_office_telephone\": \"(021) 7218000\",\n" + //
                                        "      \"organization_email\": \"admin@polri.gov.id\",\n" + //
                                        "      \"organization_date_establishment\": \"1945-08-21\",\n" + //
                                        "      \"organization_jurisdiction_level_id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\n"
                                        + //
                                        "      \"organization_total_personnel\": 100000,\n" + //
                                        "      \"organization_parent_id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\"\n" + //
                                        "    }\n" + //
                                        "  ],\n" + //
                                        "  \"size\": 20,\n" + //
                                        "  \"totalItems\": 1,\n" + //
                                        "  \"totalPages\": 1,\n" + //
                                        "  \"currentPage\": 0\n" + //
                                        "}"))),

                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
        })
        public ResponseEntity<LinkedHashMap<String, Object>> getDataOrganization(
                        @RequestParam(required = false) String keywords,
                        @RequestParam(required = true, defaultValue = "0") Integer page,
                        @RequestParam(required = true, defaultValue = "20") Integer size) {
                return organizationServices.serviceGetDataOrganization(keywords, page, size);
        }
}
