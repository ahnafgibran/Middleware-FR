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
import id.grit.facereco.model.request.LicenseRequest;
import id.grit.facereco.services.LicenseServices;

@RestController
@Validated
@CrossOrigin(maxAge = 3600)
@RequestMapping("api/v2/license")
@Tag(name = "License", description = "")
public class LicenseController {

        private LicenseServices licenseServices;

        public LicenseController(LicenseServices licenseServices) {
                this.licenseServices = licenseServices;
        }

        @PostMapping("/create")
        @Operation(summary = "Create a new License", description = "Endpoint untuk membuat data license baru")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Berhasil Menambahkan Data License Baru", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Menambahkan Data License\" }"))),

                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
        })
        public ResponseEntity<LinkedHashMap<String, Object>> createLicense(
                        @RequestBody(required = true) LicenseRequest licenseRequest,
                        HttpServletRequest request) {
                return licenseServices.serviceCreateData(licenseRequest, request);
        }

        @PutMapping("/update/{uuid}")
        @Operation(summary = "Update license data", description = "Endpoint untuk mengubah data license")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Berhasil Mengubah Data License", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Mengubah Data License\" }"))),

                        @ApiResponse(responseCode = "404", description = "Data License Tidak Ditemukan", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Data License Tidak Ditemukan\" }"))),

                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
        })
        public ResponseEntity<LinkedHashMap<String, Object>> updateLicense(@PathVariable UUID uuid,
                        @RequestBody(required = true) LicenseRequest licenseRequest,
                        HttpServletRequest request) {
                return licenseServices.serviceUpdateData(uuid, licenseRequest, request);
        }

        @DeleteMapping("/delete/{id}")
        @Operation(summary = "Delete License Data", description = "Endpoint untuk menghapus data license")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Berhasil Menghapus Data License", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Menghapus Data License\" }"))),

                        @ApiResponse(responseCode = "404", description = "Data License Tidak Ditemukan", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Data License Tidak Ditemukan\" }"))),

                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

                        @ApiResponse(responseCode = "409", description = "Conflict Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Gagal menghapus data, karena data ini masih digunakan untuk referensi data lain\" }"))),

                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
        })
        public ResponseEntity<LinkedHashMap<String, Object>> deleteLicense(@PathVariable UUID uuid,
                        @RequestBody(required = true) LicenseRequest licenseRequest) {
                return licenseServices.serviceDeleteData(uuid);
        }

        @GetMapping("/get-list")
        @Operation(summary = "Get License List Data", description = "Endpoint untuk mendapatkan list data license")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Berhasil Mengambil Data License", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{\n"
                                        + //
                                        "  \"status\": \"SUKSES\",\n" + //
                                        "  \"message\": \"Berhasil Mengambil Data License\",\n" + //
                                        "  \"data\": [\n" + //
                                        "    {\n" + //
                                        "      \"id\": \"dd61d9aa-9fd6-462a-98a8-a2a8338ded3f\",\n" + //
                                        "      \"license_name\": \"LICENSE MIDDLEWARE\",\n" + //
                                        "      \"license_description\": \"License Enterprise Penggunaan Middleware\",\n"
                                        + //
                                        "      \"license_serial_number\": \"SN-MDW-24\",\n" + //
                                        "      \"license_activated_date\": \"2024-08-21\",\n" + //
                                        "      \"license_expired_date\": null\n" +

                                        "    }\n" + //
                                        "  ],\n" + //
                                        "  \"size\": 20,\n" + //
                                        "  \"totalItems\": 1,\n" + //
                                        "  \"totalPages\": 1,\n" + //
                                        "  \"currentPage\": 0\n" + //
                                        "}"))),

                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
        })
        public ResponseEntity<LinkedHashMap<String, Object>> getDataLicense(
                        @RequestParam(required = false) String keywords,
                        @RequestParam(required = true, defaultValue = "0") Integer page,
                        @RequestParam(required = true, defaultValue = "20") Integer size) {
                return licenseServices.serviceGetDataLicense(keywords, page, size);
        }
}
