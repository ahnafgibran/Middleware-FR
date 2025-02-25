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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import id.grit.facereco.model.request.AuthRequest;
import id.grit.facereco.model.request.RegisterAuthRequest;
import id.grit.facereco.model.request.UpdateAuthRequest;
import id.grit.facereco.model.request.UpdatePasswordRequest;
import id.grit.facereco.services.AuthServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@Validated
@CrossOrigin(maxAge = 3600)
@RequestMapping("/api/v2/auth")
@Tag(name = "Authentication", description = "")
public class AuthController {

    private final AuthServices authService;

    public AuthController(AuthServices authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login User", description = "Endpoint Login User baru")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil Login", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{\n"
                    +
                    "  \"status\": \"SUKSES\",\n" +
                    "  \"message\": \"Authentication successful\",\n" +
                    "  \"data\": {\n" +
                    "    \"email\": \"example@gmail.com\",\n" +
                    "    \"token\": \token\",\n" +
                    "    \"issued_at\": \"2024-12-17T04:21:47.223+00:00\"\n" +
                    "  }\n" +
                    "}"))),

            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
    })
    public ResponseEntity<LinkedHashMap<String, Object>> login(@RequestBody AuthRequest authRequest) {
        return authService.authenticate(authRequest);
    }

    @PostMapping("/register")
    @Operation(summary = "Register User", description = "Endpoint register user baru")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil Menambahkan Data User Baru", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"SUKSES\", \"message\": \"Berhasil Menambahkan Data User\" }"))),

            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Request Tidak Sesuai\" }"))),

            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedHashMap.class), examples = @ExampleObject(value = "{ \"status\": \"ERROR\", \"message\": \"Error message\" }")))
    })
    public ResponseEntity<LinkedHashMap<String, Object>> registerUser(
            @RequestBody RegisterAuthRequest registerRequest,
            HttpServletRequest request) {
        return authService.registerUser(registerRequest, request);
    }

    @PutMapping("/update/{uuid}")
    public ResponseEntity<LinkedHashMap<String, Object>> updateUser(@PathVariable UUID uuid,
            @RequestBody UpdateAuthRequest updateAuthRequest) {
        return authService.updateUser(uuid, updateAuthRequest);
    }

    @PutMapping("/update-password/{uuid}")
    public ResponseEntity<LinkedHashMap<String, Object>> updateUserPassword(@PathVariable UUID uuid,
            @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        return authService.updateUserPassword(uuid, updatePasswordRequest);
    }
}
