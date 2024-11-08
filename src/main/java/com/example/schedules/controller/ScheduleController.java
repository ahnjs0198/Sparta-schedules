package com.example.schedules.controller;

import com.example.schedules.dto.ScheduleRequestDto;
import com.example.schedules.dto.ScheduleResponseDto;
import com.example.schedules.entity.Schedule;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final Map<Long, Schedule> scheduleList = new HashMap<>();

    @PostMapping
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto dto) {

        Long scheduleId = scheduleList.isEmpty() ? 1 : Collections.max(scheduleList.keySet()) + 1;

        Schedule schedule = new Schedule(scheduleId, dto.getTodo(), dto.getWriter(), dto.getPassword(), LocalDateTime.now(), LocalDateTime.now());

        scheduleList.put(scheduleId, schedule);

        return new ScheduleResponseDto(schedule);
    }

    @GetMapping("/{id}")
    public ScheduleResponseDto findScheduleById(@PathVariable Long id) {

        Schedule schedule = scheduleList.get(id);

        return new ScheduleResponseDto(schedule);
    }

    @PutMapping("/{id}")
    public ScheduleResponseDto updateScheduleById(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto dto
    ) {
        Schedule schedule = scheduleList.get(id);

        schedule.update(dto);

        return new ScheduleResponseDto(schedule);
    }

    @DeleteMapping("/{id}")
    public void deleteSchedule(@PathVariable Long id) {

        scheduleList.remove(id);

    }
}
