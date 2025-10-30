package com.caffein.schoolcourseservice.dto.schedule.mapper;

import com.caffein.schoolcourseservice.dto.schedule.ScheduleDTO;
import com.caffein.schoolcourseservice.model.Schedule;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMapper {

    public ScheduleDTO toDTO(Schedule schedule) {
        if (schedule == null) return null;

        return ScheduleDTO.builder()
                .id(schedule.getId())
                .courseId(schedule.getCourse() != null ? schedule.getCourse().getId() : null)
                .dayOfWeek(schedule.getDayOfWeek())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .roomNumber(schedule.getRoomNumber())
                .build();
    }
}