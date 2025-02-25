package id.grit.facereco.services;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.ListenableFuture;

import io.grpc.ManagedChannel;
import io.infran.driverinfranfaceid.DeleteUserRequest;
import io.infran.driverinfranfaceid.DeleteUserResponse;
import io.infran.driverinfranfaceid.DriverInfranFaceIDGrpc;
import io.infran.driverinfranfaceid.DriverInfranFaceIDGrpc.DriverInfranFaceIDFutureStub;
import io.infran.driverinfranfaceid.IdentifyManyRequest;
import io.infran.driverinfranfaceid.IdentifyManyResponse;
import io.infran.driverinfranfaceid.IdentifyOneRequest;
import io.infran.driverinfranfaceid.IdentifyOneResponse;
import io.infran.driverinfranfaceid.RegisterUserRequest;
import io.infran.driverinfranfaceid.RegisterUserResponse;

@Service
public class FaceRecService {

    final ManagedChannel channel;

    private DriverInfranFaceIDFutureStub stub;

    @Value("${facereco.engine.max-timeout}")
    private long maxTimeout;

    public FaceRecService(ManagedChannel facerecChannel) {
        channel = facerecChannel;
    }

    private void buildStub() {
        stub = DriverInfranFaceIDGrpc.newFutureStub(channel);
    }

    @Async
    public ListenableFuture<IdentifyOneResponse> identifyOne(byte[] imgByte)
            throws UnsupportedEncodingException {

        if (stub == null) {
            buildStub();
        }

        String imgData = Base64.getEncoder().encodeToString(imgByte);
        
        ListenableFuture<IdentifyOneResponse> resp;
        resp = stub.withDeadlineAfter(maxTimeout, TimeUnit.SECONDS)
                .identifyOne(IdentifyOneRequest.newBuilder()
                        .setTrxID("")
                        .setTenantID("dea4e044-17c7-4b9e-8e03-1e77fb5b9a50")
                        .setTimestamp("")
                        .setDeviceID("")
                        .setImgData(imgData)
                        .setFaceDetector(0)
                        .build());

        return resp;

    }

    @Async
    public ListenableFuture<IdentifyManyResponse> identifyMany(byte[] imgByte, int fetchLimit)
            throws UnsupportedEncodingException {

        if (stub == null) {
            buildStub();
        }

        String imgData = Base64.getEncoder().encodeToString(imgByte);

        ListenableFuture<IdentifyManyResponse> resp;
        resp = stub.withDeadlineAfter(maxTimeout, TimeUnit.SECONDS)
                .identifyMany(IdentifyManyRequest.newBuilder()
                        .setTrxID("")
                        .setTenantID("dea4e044-17c7-4b9e-8e03-1e77fb5b9a50")
                        .setTimestamp("")
                        .setDeviceID("")
                        .setImgData(imgData)
                        .setFaceDetector(0)
                        .setNumResult(fetchLimit) // Setting the number of results to fetchLimit
                        .build());

        return resp;
    }


    public ListenableFuture<RegisterUserResponse> registerFace(String descriptions, byte[] imgByte) {
        if (stub == null) {
            buildStub();
        }
        
        if ( descriptions == null ) {
        	descriptions = "data-for-poc";
        }

        String imgData = Base64.getEncoder().encodeToString(imgByte);
        ListenableFuture<RegisterUserResponse> resp;
        resp = stub.withDeadlineAfter(maxTimeout, TimeUnit.SECONDS).registerUser(RegisterUserRequest.newBuilder()
                .setTrxID("")
                .setTenantID("dea4e044-17c7-4b9e-8e03-1e77fb5b9a50")
                .setTimestamp("")
                .setDeviceID("")
                .setImgData(imgData)
                .setFaceDetector(0)
                .setName("poc")
                .setPassword("password@poc")
                .setUserID("poc")
                .setDescription(descriptions)
                .build());

        return resp;

    }

    public ListenableFuture<DeleteUserResponse> deleteUser(String userID) {
        if (stub == null) {
            buildStub();
        }

        DeleteUserRequest request = DeleteUserRequest.newBuilder()
                .setTenantID("dea4e044-17c7-4b9e-8e03-1e77fb5b9a50")
                .setUserID(userID)
                .build();

        ListenableFuture<DeleteUserResponse> resp = stub
                .withDeadlineAfter(maxTimeout, TimeUnit.SECONDS)
                .deleteUser(request);

        return resp;
    }

    public GrpcServiceType getType() {
        return GrpcServiceType.FACERECO;
    }

    public enum GrpcServiceType {
        FACEDEX,
        FACERECO,
    }
}
