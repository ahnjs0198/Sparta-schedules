package com.example.schedules.repository;

import com.example.schedules.dto.ScheduleResponseDto;
import com.example.schedules.entity.Schedule;

import java.util.List;

public interface ScheduleRepository {
    List<ScheduleResponseDto> findAll(String writer, String updatedAt);

    Schedule findScheduleByIdOrElseThrow(Long id);

    ScheduleResponseDto saveSchedule(Schedule schedule);

    int deleteSchedule(Long id, String password);

    int updateSchedule(Long id, String todo, String writer, String password);
}
