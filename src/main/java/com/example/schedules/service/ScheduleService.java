package com.example.schedules.service;

import com.example.schedules.dto.ScheduleRequestDto;
import com.example.schedules.dto.ScheduleResponseDto;

import java.util.List;

public interface ScheduleService {
    List<ScheduleResponseDto> findAll(String writer, String updatedAt);

    ScheduleResponseDto findScheduleById(Long id);

    ScheduleResponseDto saveSchedule(ScheduleRequestDto scheduleRequestDto);

    void deleteSchedule(Long id, String password);

    ScheduleResponseDto updateSchedule(Long id, String todo, String writer, String password);
}
