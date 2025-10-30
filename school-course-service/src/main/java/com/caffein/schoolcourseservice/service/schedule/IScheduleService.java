package com.caffein.schoolcourseservice.service.schedule;

import com.caffein.schoolcourseservice.dto.schedule.ScheduleCreateDTO;
import com.caffein.schoolcourseservice.dto.schedule.ScheduleDTO;

import java.util.UUID;

public interface IScheduleService {
    ScheduleDTO addScheduleToCourse(UUID courseId, ScheduleCreateDTO createDTO);
    ScheduleDTO updateSchedule(UUID id, ScheduleCreateDTO updateDTO);
    void deleteSchedule(UUID id);
}