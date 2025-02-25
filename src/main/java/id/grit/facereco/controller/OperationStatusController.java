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
import id.grit.facereco.model.request.OperationStatusRequest;
import id.grit.facereco.services.OperationStatusServices;

@RestController
@Validated
@CrossOrigin(maxAge = 3600)
@RequestMapping("api/v2/operation-status")
@Tag(name = "Operation Status", description = "")
public class OperationStatusController {

    private OperationStatusServices operationStatusServices;

    public OperationStatusController(OperationStatusServices operationStatusServices) {
        this.operationStatusServices = operationStatusServices;
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new operation status", description = "Endpoint untuk membuat data operation status baru")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil Menambahkan Data operation status Baru", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Menambahkan Data Operation Status\" }"))),

            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
    })
    public ResponseEntity<LinkedHashMap<String, Object>> createOperationStatus(
            @RequestBody(required = true) OperationStatusRequest operationStatusRequest, HttpServletRequest request) {
        return operationStatusServices.serviceCreateData(operationStatusRequest, request);
    }

    @PutMapping("/update/{uuid}")
    @Operation(summary = "Update operation status data", description = "Endpoint untuk mengubah data operation status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil Mengubah Data Operation Status", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Mengubah Data Operation Status\" }"))),

            @ApiResponse(responseCode = "404", description = "Data Operation Status Tidak Ditemukan", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Data Operation Status Tidak Ditemukan\" }"))),

            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
    })
    public ResponseEntity<LinkedHashMap<String, Object>> updateOperationStatus(
            @PathVariable UUID uuid, @RequestBody(required = true) OperationStatusRequest operationStatusRequest,
            HttpServletRequest request) {
        return operationStatusServices.serviceUpdateData(uuid, operationStatusRequest, request);
    }

    @GetMapping("/get-list")
    @Operation(summary = "Get Operation Status List Data", description = "Endpoint untuk mendapatkan list data operation status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil Mengambil Data Operation Status", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Mengambil Data Operation Status\", \"data\":[{ \"id\": \"34b3598a-de36-4585-a26a-7db5acf35f7a\", \"operation_status_name\": \"Selesai\" ,\"operation_status_code\": \"SELESAI\",\"flag_active_operation_status\":true }]}"))),

            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
    })
    public ResponseEntity<LinkedHashMap<String, Object>> getListOperationStatus() {
        return operationStatusServices.serviceGetListData();
    }

}
