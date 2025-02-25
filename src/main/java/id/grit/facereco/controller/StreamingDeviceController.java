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
import id.grit.facereco.model.request.StreamingDeviceRequest;
import id.grit.facereco.services.StreamingDevicesServices;

@RestController
@Validated
@CrossOrigin(maxAge = 3600)
@RequestMapping("api/v2/streaming-device")
@Tag(name = "Streaming Device", description = "")
public class StreamingDeviceController {

        private StreamingDevicesServices streamingDevicesServices;

        public StreamingDeviceController(StreamingDevicesServices streamingDevicesServices) {
                this.streamingDevicesServices = streamingDevicesServices;
        }

        @PostMapping("/create")
        @Operation(summary = "Create a new Streaming Device", description = "Endpoint untuk membuat data streaming device baru")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Berhasil Menambahkan Data Streaming Device Baru", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Menambahkan Data Streaming Device\" }"))),

                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
        })
        public ResponseEntity<LinkedHashMap<String, Object>> createStreamingDevice(
                        @RequestBody(required = true) StreamingDeviceRequest streamingDeviceRequest,
                        HttpServletRequest request) {
                return streamingDevicesServices.serviceCreateData(streamingDeviceRequest, request);
        }

        @PutMapping("/update/{uuid}")
        @Operation(summary = "Update streaming device data", description = "Endpoint untuk mengubah data streaming device")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Berhasil Mengubah Data Streaming Device", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Mengubah Data Streaming Device\" }"))),

                        @ApiResponse(responseCode = "404", description = "Data Streaming Device Tidak Ditemukan", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Data Streaming Device Tidak Ditemukan\" }"))),

                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
        })
        public ResponseEntity<LinkedHashMap<String, Object>> updateStreamingDevice(@PathVariable UUID uuid,
                        @RequestBody(required = true) StreamingDeviceRequest streamingDeviceRequest,
                        HttpServletRequest request) {
                return streamingDevicesServices.serviceUpdateData(uuid, streamingDeviceRequest, request);
        }

        @DeleteMapping("/delete/{uuid}")
        @Operation(summary = "Delete Streaming Device Data", description = "Endpoint untuk menghapus data streaming device")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Berhasil Menghapus Data Streaming Device", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Menghapus Data Streaming Device\" }"))),

                        @ApiResponse(responseCode = "404", description = "Data Streaming Device Tidak Ditemukan", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Data Streaming Device Tidak Ditemukan\" }"))),

                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

                        @ApiResponse(responseCode = "409", description = "Conflict Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Gagal menghapus data, karena data ini masih digunakan untuk referensi data lain\" }"))),

                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
        })
        public ResponseEntity<LinkedHashMap<String, Object>> deleteStreamingDevice(@PathVariable UUID uuid) {
                return streamingDevicesServices.serviceDeleteData(uuid);
        }

        @GetMapping("/get-list")
        @Operation(summary = "Get Streaming Device Data", description = "Endpoint untuk mendapatkan list data streaming deivce")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Berhasil Mengambil Data Streaming Device", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{\n"
                                        + //
                                        "  \"status\": \"SUKSES\",\n" + //
                                        "  \"message\": \"Berhasil Mengambil Data Streaming Device\",\n" + //
                                        "  \"data\": [\n" + //
                                        "    {\n" + //
                                        "      \"id\": \"dd61d9aa-9fd6-462a-98a8-a2a8338ded3f\",\n" + //
                                        "      \"device_name\": \"HIK-VISION 01\",\n" + //
                                        "      \"ip_address\": \"http://127.0.0.1:9000\",\n" + //
                                        "      \"device_url_rtsp\": \"http://172.29.89.202:8080/index/api/webrtc?app=live&stream=test-cctv-dahua-sub2&type=play\",\n"
                                        + //
                                        "      \"device_organization_id\": \"dd61d9aa-9fd6-462a-98a8-a2a8338ded3f\",\n"
                                        + //
                                        "      \"device_type_id\": \"dd61d9aa-9fd6-462a-98a8-a2a8338ded3f\",\n" +
                                        "      \"operation_id\": \"dd61d9aa-9fd6-462a-98a8-a2a8338ded3f\"\n" +

                                        "    }\n" + //
                                        "  ],\n" + //
                                        "  \"size\": 20,\n" + //
                                        "  \"totalItems\": 1,\n" + //
                                        "  \"totalPages\": 1,\n" + //
                                        "  \"currentPage\": 0\n" + //
                                        "}"))),

                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
        })
        public ResponseEntity<LinkedHashMap<String, Object>> getDataStreamingDevice(
                        @RequestParam(required = false) String keywords,
                        @RequestParam(required = false) String organizationId,
                        @RequestParam(required = true, defaultValue = "0") Integer page,
                        @RequestParam(required = true, defaultValue = "20") Integer size) {
                return streamingDevicesServices.serviceGetDataStreamingDevice(keywords, organizationId, page, size);
        }

}
