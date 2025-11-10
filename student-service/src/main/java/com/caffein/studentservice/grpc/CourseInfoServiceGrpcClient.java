package com.caffein.studentservice.grpc;

import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class CourseInfoServiceGrpcClient {

    private final com.caffein.studentservice.grpc.CourseInformationServiceGrpc.CourseInformationServiceBlockingStub courseInformationServiceBlockingStub;

    public com.caffein.studentservice.grpc.CourseResponse getCourseById(UUID courseId) {
        log.info("Fetching course info for courseId: {}", courseId);
        try {
            com.caffein.studentservice.grpc.CourseRequest request = com.caffein.studentservice.grpc.CourseRequest.newBuilder()
                    .setCourseId(courseId.toString())
                    .build();
            return courseInformationServiceBlockingStub.getCourseById(request);
        } catch (StatusRuntimeException e) {
            log.error("gRPC error while fetching course info for courseId {}: {}", courseId, e.getStatus());
            throw new IllegalStateException("Failed to retrieve course information. Please try again later.", e);
        }
    }
}
