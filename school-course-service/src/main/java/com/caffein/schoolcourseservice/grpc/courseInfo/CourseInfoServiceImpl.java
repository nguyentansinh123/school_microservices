package com.caffein.schoolcourseservice.grpc.courseInfo;

import com.caffein.schoolcourseservice.dto.course.CourseDTO;
import com.caffein.schoolcourseservice.dto.subject.SubjectDTO;
import com.caffein.schoolcourseservice.grpc.*;
import com.caffein.schoolcourseservice.service.courseService.ICourseService;
import com.caffein.schoolcourseservice.service.subjectService.ISubjectService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

import java.util.List;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class CourseInfoServiceImpl extends CourseInformationServiceGrpc.CourseInformationServiceImplBase {

    private final ICourseService courseService;
    private final ISubjectService subjectService;

    @Override
    public void getCourseById(CourseRequest request, StreamObserver<CourseResponse> responseObserver) {
        try {
            CourseDTO courseDTO = courseService.getCourseById(UUID.fromString(request.getCourseId()));
            CourseResponse response = convertToCourseResponse(courseDTO);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getAvailableCourses(CoursesRequest request, StreamObserver<CourseResponse> responseObserver) {
        try {
            String academicYear = request.hasAcademicYear() ? request.getAcademicYear().getValue() : null;
            String semester = request.hasSemester() ? request.getSemester().getValue() : null;

            List<CourseDTO> courses = courseService.getAllCourses(null, null, academicYear, semester);

            courses.stream()
                    .map(this::convertToCourseResponse)
                    .forEach(responseObserver::onNext);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
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

    private CourseResponse convertToCourseResponse(CourseDTO dto) {
        CourseResponse.Builder builder = CourseResponse.newBuilder()
                .setId(dto.getId().toString())
                .setName(dto.getName())
                .setAcademicYear(dto.getAcademicYear())
                .setSemester(dto.getSemester())
                .setMaxCapacity(dto.getMaxCapacity());

        if (dto.getSubject() != null) {
            builder.setSubject(convertToSubjectResponse(dto.getSubject()));
        }

        if (dto.getTeacher() != null) {
            builder.setTeacher(TeacherResponse.newBuilder()
                    .setId(dto.getTeacher().getId().toString())
                    .setFirstName(dto.getTeacher().getFirstName())
                    .setLastName(dto.getTeacher().getLastName())
                    .build());
        }

        if (dto.getSchedules() != null) {
            dto.getSchedules().forEach(scheduleDTO -> builder.addSchedules(
                    ScheduleResponse.newBuilder()
                            .setDayOfWeek(scheduleDTO.getDayOfWeek().name())
                            .setStartTime(scheduleDTO.getStartTime().toString())
                            .setEndTime(scheduleDTO.getEndTime().toString())
                            .setRoomNumber(scheduleDTO.getRoomNumber() != null ? scheduleDTO.getRoomNumber() : "")
                            .build()
            ));
        }

        return builder.build();
    }

    private SubjectResponse convertToSubjectResponse(SubjectDTO dto) {
        return SubjectResponse.newBuilder()
                .setId(dto.getId().toString())
                .setName(dto.getName())
                .setDepartment(dto.getDepartment())
                .build();
    }
}