package id.grit.facereco.services;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import id.grit.facereco.config.JwtUtil;
import id.grit.facereco.entity.AuthEntity;
import id.grit.facereco.entity.Gender;
import id.grit.facereco.entity.JurisdictionLevel;
import id.grit.facereco.entity.Organization;
import id.grit.facereco.entity.PermissionGroup;
import id.grit.facereco.model.request.AuthRequest;
import id.grit.facereco.model.request.RegisterAuthRequest;
import id.grit.facereco.model.request.UpdateAuthRequest;
import id.grit.facereco.model.request.UpdatePasswordRequest;
import id.grit.facereco.model.response.RegisterAuthResponse;
import id.grit.facereco.repository.AuthRepository;
import id.grit.facereco.repository.GenderRepository;
import id.grit.facereco.repository.JurisdictionRepository;
import id.grit.facereco.repository.OrganizationRepository;
import id.grit.facereco.repository.PermissionGroupRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthServices {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private PermissionGroupRepository permissionGroupRepository;

    @Autowired
    private final GenderRepository genderRepository;

    @Autowired
    private final AuthRepository authRepository;

    @Autowired
    private JurisdictionRepository jurisdictionRepository;

    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServices(AuthRepository authRepository, GenderRepository genderRepository,
            BCryptPasswordEncoder passwordEncoder, JurisdictionRepository jurisdictionRepository, JwtUtil jwtUtil) {
        this.authRepository = authRepository;
        this.jurisdictionRepository = jurisdictionRepository;
        this.genderRepository = genderRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // 1. Authenticate User
    public ResponseEntity<LinkedHashMap<String, Object>> authenticate(AuthRequest authRequest) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            // Cek keberadaan user berdasarkan email
            AuthEntity user = authRepository.findByEmail(authRequest.getEmail())
                    .orElse(null);

            // Validasi password
            if (user == null || !passwordEncoder.matches(authRequest.getPassword(), user.getUser_password())) {
                response.put("status", "ERROR");
                response.put("message", "Invalid email or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Buat token JWT
            String token = jwtUtil.createToken(user.getUuid().toString());
            response.put("status", "SUKSES");
            response.put("message", "Authentication successful");
            response.put("data", buildResponseData(user, token));
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 2. Register User
    public ResponseEntity<LinkedHashMap<String, Object>> registerUser(RegisterAuthRequest registerRequest,
            HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {

            if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
                response.put("status", "ERROR");
                response.put("message", "Password and confirm password do not match");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (authRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
                response.put("status", "ERROR");
                response.put("message", "Email already exists");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Gender gender = genderRepository.findByUuid(UUID.fromString(registerRequest.getUser_gender_id()))
                    .orElseThrow(() -> new RuntimeException("Gender not found"));

            Organization organization = organizationRepository
                    .findByUuid(UUID.fromString(registerRequest.getUser_organization_id()))
                    .orElseThrow(() -> new RuntimeException("Organization not found"));

            PermissionGroup permissionGroup = permissionGroupRepository
                    .findByUuid(UUID.fromString(registerRequest.getUser_permission_id()))
                    .orElseThrow(() -> new RuntimeException("Permission Group not found"));

            AuthEntity newUser = new AuthEntity();
            newUser.setUuid(UUID.randomUUID());
            newUser.setEmail(registerRequest.getEmail().toLowerCase());
            newUser.setUser_full_name(registerRequest.getFullName());
            newUser.setUser_password(passwordEncoder.encode(registerRequest.getPassword()));
            newUser.setUser_phone(registerRequest.getPhone());
            newUser.set_active(true);
            newUser.setUser_gender_id(gender);
            newUser.setUser_organization_id(organization);
            newUser.setUser_permission_id(permissionGroup);
            newUser.setLast_modified_by(UUID.randomUUID());
            authRepository.save(newUser);

            RegisterAuthResponse registerResponse = new RegisterAuthResponse();
            registerResponse.setEmail(newUser.getEmail());
            registerResponse.setFullName(newUser.getUser_full_name());
            registerResponse.setPhone(newUser.getUser_phone());
            registerResponse.setUser_gender_id(newUser.getUser_gender_id().getUuid().toString());
            registerResponse.setUser_organization_id(newUser.getUser_organization_id().getUuid().toString());
            registerResponse.setUser_permission_id(newUser.getUser_permission_id().getUuid().toString());
            registerResponse.set_active(newUser.is_active());
            // registerResponse.setUser_gender_id(newUser.getUser_gender_id().getUuid().toString());
            // registerResponse.setIsActive(newUser.);

            response.put("status", "Succes");
            response.put("message", "User registered successfully");
            response.put("data", registerResponse);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 3. Update User Data
    public ResponseEntity<LinkedHashMap<String, Object>> updateUser(UUID uuid, UpdateAuthRequest updateAuthRequest) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            Optional<AuthEntity> optional = authRepository.findByUuid(uuid);
            if (optional.isPresent()) {
                AuthEntity existingData = optional.get();

                // Validasi Password
                if (passwordEncoder.matches(updateAuthRequest.getPassword(), existingData.getUser_password())) {
                    String message = "Berhasil Mengubah Data Diri";
                    AuthEntity updateData = new AuthEntity();

                    Gender gender = genderRepository.findByUuid(UUID.fromString(updateAuthRequest.getUser_gender_id()))
                            .orElseThrow(() -> new RuntimeException("Gender not found"));

                    Organization organization = organizationRepository
                            .findByUuid(UUID.fromString(updateAuthRequest.getUser_organization_id()))
                            .orElseThrow(() -> new RuntimeException("Organization not found"));

                    PermissionGroup permissionGroup = permissionGroupRepository
                            .findByUuid(UUID.fromString(updateAuthRequest.getUser_permission_id()))
                            .orElseThrow(() -> new RuntimeException("Permission Group not found"));

                    Date currentDate = new Date();
                    updateData.setId(existingData.getId());
                    updateData.setUuid(existingData.getUuid());
                    updateData.setDate_modified(currentDate);
                    updateData.setDate_created(existingData.getDate_created());
                    updateData.setLast_modified_by(existingData.getLast_modified_by());
                    updateData.setUser_full_name(updateAuthRequest.getFullName());
                    updateData.setEmail(updateAuthRequest.getEmail());
                    updateData.setUser_phone(updateAuthRequest.getPhone());
                    updateData.setUser_password(passwordEncoder.encode(updateAuthRequest.getPassword()));
                    updateData.setUser_gender_id(gender);
                    updateData.setUser_organization_id(organization);
                    updateData.setUser_permission_id(permissionGroup);

                    // save the update entity
                    authRepository.save(updateData);
                    response.put("status", "SUKSES");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {

                    String message = "Kata Sandi Tidak Sesuai";
                    response.put("status", "ERROR");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }

            } else {
                String message = "Akun Tidak Ditemukan";
                response.put("status", "ERROR");
                response.put("message", message);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 4. Update Password
    public ResponseEntity<LinkedHashMap<String, Object>> updateUserPassword(UUID uuid,
            UpdatePasswordRequest updatePasswordRequest) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            Optional<AuthEntity> optional = authRepository.findByUuid(uuid);
            if (optional.isPresent()) {
                AuthEntity existingData = optional.get();

                // Validasi Password
                if (passwordEncoder.matches(updatePasswordRequest.getPasswordOld(), existingData.getUser_password())) {

                    if (updatePasswordRequest.getPasswordNew().equals(updatePasswordRequest.getPasswordOld())) {
                        String message = "Kata Sandi Baru Harus Berbeda Dengan Kata Sandi Lama";
                        response.put("status", "ERROR");
                        response.put("message", message);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    } else {
                        String message = "Berhasil Mengubah Data Diri";
                        AuthEntity updateData = new AuthEntity();

                        Date currentDate = new Date();
                        updateData.setId(existingData.getId());
                        updateData.setUuid(existingData.getUuid());
                        updateData.setDate_modified(currentDate);
                        updateData.setDate_created(existingData.getDate_created());
                        updateData.setLast_modified_by(existingData.getLast_modified_by());
                        updateData.setUser_full_name(existingData.getUser_full_name());
                        updateData.setEmail(existingData.getEmail());
                        updateData.setUser_phone(existingData.getUser_phone());
                        updateData.setUser_password(passwordEncoder.encode(updatePasswordRequest.getPasswordNew()));
                        updateData.setUser_gender_id(existingData.getUser_gender_id());
                        updateData.setUser_organization_id(existingData.getUser_organization_id());
                        updateData.setUser_permission_id(existingData.getUser_permission_id());

                        // save the update entity
                        authRepository.save(updateData);
                        response.put("status", "SUKSES");
                        response.put("message", message);
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    }

                } else {
                    String message = "Kata Sandi Tidak Sesuai";
                    response.put("status", "ERROR");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }

            } else {
                String message = "Akun Tidak Ditemukan";
                response.put("status", "ERROR");
                response.put("message", message);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private LinkedHashMap<String, Object> buildResponseData(AuthEntity user, String token) {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        JurisdictionLevel jurisdictionLevel = jurisdictionRepository
                .findByUuid(user.getUser_organization_id().getOrganization_jurisdiction_level_id())
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        data.put("id", user.getUuid());
        data.put("nama", user.getUser_full_name());
        data.put("phone", user.getUser_phone());
        data.put("gender_id", user.getUser_gender_id().getUuid());
        data.put("jurisdiction_level_id", jurisdictionLevel.getUuid());
        data.put("jurisdiction_level_code", jurisdictionLevel.getJurisdiction_code());
        data.put("organization_id", user.getUser_organization_id().getUuid());
        data.put("permission_id", user.getUser_permission_id().getUuid());
        data.put("permission_flag", user.getUser_permission_id().getPermission_flag_object());
        data.put("email", user.getEmail());
        data.put("token", token);
        data.put("issued_at", new Date());
        return data;
    }
}
