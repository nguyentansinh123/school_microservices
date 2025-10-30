package com.caffein.schoolcourseservice.service.schedule;

import com.caffein.schoolcourseservice.dto.schedule.ScheduleCreateDTO;
import com.caffein.schoolcourseservice.dto.schedule.ScheduleDTO;
import com.caffein.schoolcourseservice.dto.schedule.mapper.ScheduleMapper;
import com.caffein.schoolcourseservice.model.Course;
import com.caffein.schoolcourseservice.model.Schedule;
import com.caffein.schoolcourseservice.repository.CourseRepository;
import com.caffein.schoolcourseservice.repository.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService implements IScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CourseRepository courseRepository;
    private final ScheduleMapper scheduleMapper;

    @Override
    @Transactional
    public ScheduleDTO addScheduleToCourse(UUID courseId, ScheduleCreateDTO createDTO) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseId));

        // Validate time logic
        if (createDTO.getStartTime().isAfter(createDTO.getEndTime())) {
            throw new IllegalArgumentException("Start time cannot be after end time");
        }

        // Check for conflicts with existing schedules for this course
        List<Schedule> existingSchedules = scheduleRepository.findByCourseIdAndDayOfWeek(courseId, createDTO.getDayOfWeek());
        for (Schedule existing : existingSchedules) {
            if (hasTimeConflict(createDTO, existing)) {
                throw new IllegalArgumentException("Schedule conflicts with existing schedule for this course");
            }
        }

        Schedule schedule = Schedule.builder()
                .course(course)
                .dayOfWeek(createDTO.getDayOfWeek())
                .startTime(createDTO.getStartTime())
                .endTime(createDTO.getEndTime())
                .roomNumber(createDTO.getRoomNumber())
                .build();

        Schedule savedSchedule = scheduleRepository.save(schedule);
        log.info("Added schedule for course: {} on {} from {} to {}",
                course.getName(), createDTO.getDayOfWeek(), createDTO.getStartTime(), createDTO.getEndTime());

        return scheduleMapper.toDTO(savedSchedule);
    }

    @Override
    @Transactional
    public ScheduleDTO updateSchedule(UUID id, ScheduleCreateDTO updateDTO) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with ID: " + id));

        // Validate time logic
        if (updateDTO.getStartTime().isAfter(updateDTO.getEndTime())) {
            throw new IllegalArgumentException("Start time cannot be after end time");
        }

        // Check for conflicts with other schedules for the same course (excluding current schedule)
        List<Schedule> existingSchedules = scheduleRepository.findByCourseIdAndDayOfWeek(
                schedule.getCourse().getId(), updateDTO.getDayOfWeek());

        for (Schedule existing : existingSchedules) {
            if (!existing.getId().equals(id) && hasTimeConflict(updateDTO, existing)) {
                throw new IllegalArgumentException("Updated schedule conflicts with existing schedule for this course");
            }
        }

        // Update fields
        schedule.setDayOfWeek(updateDTO.getDayOfWeek());
        schedule.setStartTime(updateDTO.getStartTime());
        schedule.setEndTime(updateDTO.getEndTime());
        schedule.setRoomNumber(updateDTO.getRoomNumber());

        Schedule updatedSchedule = scheduleRepository.save(schedule);
        log.info("Updated schedule for course: {} on {} from {} to {}",
                schedule.getCourse().getName(), updateDTO.getDayOfWeek(), updateDTO.getStartTime(), updateDTO.getEndTime());

        return scheduleMapper.toDTO(updatedSchedule);
    }

    @Override
    @Transactional
    public void deleteSchedule(UUID id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with ID: " + id));

        scheduleRepository.delete(schedule);
        log.info("Deleted schedule for course: {} on {}",
                schedule.getCourse().getName(), schedule.getDayOfWeek());
    }

    private boolean hasTimeConflict(ScheduleCreateDTO newSchedule, Schedule existingSchedule) {
        return !(newSchedule.getEndTime().isBefore(existingSchedule.getStartTime()) ||
                newSchedule.getStartTime().isAfter(existingSchedule.getEndTime()));
    }
}
