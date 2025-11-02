package com.caffein.schoolcourseservice.grpc.courseInfo;

import com.caffein.schoolcourseservice.dto.subject.SubjectDTO;
import com.caffein.schoolcourseservice.grpc.*;
import com.caffein.schoolcourseservice.service.subjectService.ISubjectService;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
public class CourseInfoServiceImpl extends CourseInformationServiceGrpc.CourseInformationServiceImplBase {

    private final ISubjectService subjectService;

    public CourseInfoServiceImpl(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Override
    public void getSubjects(SubjectsRequest request, StreamObserver<SubjectResponse> responseObserver) {
        try {
            List<SubjectDTO> subjects = subjectService.getAllSubjects();
            subjects.stream()
                    .map(this::convertToSubjectResponse)
                    .forEach(responseObserver::onNext);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    private SubjectResponse convertToSubjectResponse(SubjectDTO dto) {
        return SubjectResponse.newBuilder()
                .setId(dto.getId().toString())
                .setName(dto.getName())
                .setDepartment(dto.getDepartment())
                .build();
    }
}