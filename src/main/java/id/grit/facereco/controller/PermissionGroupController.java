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
import id.grit.facereco.model.request.PermisionGroupRequest;
import id.grit.facereco.services.PermissionGroupServices;

@RestController
@Validated
@CrossOrigin(maxAge = 3600)
@RequestMapping("api/v2/permission-group")
@Tag(name = "Permission Group", description = "")
public class PermissionGroupController {

        private PermissionGroupServices permissionGroupServices;

        public PermissionGroupController(PermissionGroupServices permissionGroupServices) {
                this.permissionGroupServices = permissionGroupServices;
        }

        @PostMapping("/create")
        @Operation(summary = "Create a new Permission Group", description = "Endpoint untuk membuat data permission group baru")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Berhasil Menambahkan Data Permission Group Baru", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Menambahkan Data Permission Group\" }"))),

                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
        })
        public ResponseEntity<LinkedHashMap<String, Object>> createPermissionGroup(
                        @RequestBody(required = true) PermisionGroupRequest permisionGroupRequest,
                        HttpServletRequest request) {
                return permissionGroupServices.serviceCreateData(permisionGroupRequest, request);
        }

        @PutMapping("/update/{uuid}")
        @Operation(summary = "Update permission group data", description = "Endpoint untuk mengubah data permission group")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Berhasil Mengubah Data Permission Group", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Mengubah Data Permission Group\" }"))),

                        @ApiResponse(responseCode = "404", description = "Data Permission Group Tidak Ditemukan", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Data Permission Group Tidak Ditemukan\" }"))),

                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
        })
        public ResponseEntity<LinkedHashMap<String, Object>> updatePermissionGroup(@PathVariable UUID uuid,
                        @RequestBody(required = true) PermisionGroupRequest permisionGroupRequest,
                        HttpServletRequest request) {
                return permissionGroupServices.serviceUpdateData(uuid, permisionGroupRequest, request);
        }

        @DeleteMapping("/delete/{uuid}")
        @Operation(summary = "Delete Permission Group Data", description = "Endpoint untuk menghapus data permission group")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Berhasil Menghapus Data Permission Group", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Menghapus Data Permission Group\" }"))),

                        @ApiResponse(responseCode = "404", description = "Data Permission Group Tidak Ditemukan", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Data Permission Group Tidak Ditemukan\" }"))),

                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

                        @ApiResponse(responseCode = "409", description = "Conflict Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Gagal menghapus data, karena data ini masih digunakan untuk referensi data lain\" }"))),

                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
        })
        public ResponseEntity<LinkedHashMap<String, Object>> deletePermissionGroup(@PathVariable UUID uuid) {
                return permissionGroupServices.serviceDeleteData(uuid);
        }

        @GetMapping("/get-list")
        @Operation(summary = "Get Permission Group List Data", description = "Endpoint untuk mendapatkan list data permission group")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Berhasil Mengambil Data Permission Group", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{\n"
                                        + //
                                        "  \"status\": \"SUKSES\",\n" + //
                                        "  \"message\": \"Berhasil Mengambil Data Permission Group\",\n" + //
                                        "  \"data\": [\n" + //
                                        "    {\n" + //
                                        "      \"id\": \"dd61d9aa-9fd6-462a-98a8-a2a8338ded3f\",\n" + //
                                        "      \"permission_name\": \"Operator Admin\",\n" + //
                                        "      \"permission_flag_object\": \"{json_object_role}\",\n" + //
                                        "      \"device_organization_id\": \"dd61d9aa-9fd6-462a-98a8-a2a8338ded3f\"\n" + //

                                        "    }\n" + //
                                        "  ],\n" + //
                                        "  \"size\": 20,\n" + //
                                        "  \"totalItems\": 1,\n" + //
                                        "  \"totalPages\": 1,\n" + //
                                        "  \"currentPage\": 0\n" + //
                                        "}"))),

                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
        })
        public ResponseEntity<LinkedHashMap<String, Object>> getDataPermissionGroup(
                        @RequestParam(required = false) String keywords,
                        @RequestParam(required = false) String organizationId,
                        @RequestParam(required = true, defaultValue = "0") Integer page,
                        @RequestParam(required = true, defaultValue = "20") Integer size) {
                return permissionGroupServices.serviceGetDataPermissionGroup(keywords, organizationId, page, size);
        }

}
