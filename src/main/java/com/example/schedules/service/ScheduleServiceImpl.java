package com.example.schedules.service;

import com.example.schedules.dto.ScheduleRequestDto;
import com.example.schedules.dto.ScheduleResponseDto;
import com.example.schedules.entity.Schedule;
import com.example.schedules.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService{
    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public List<ScheduleResponseDto> findAll(String writer, String updatedAt) {
        return scheduleRepository.findAll(writer, updatedAt);
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);
        return new ScheduleResponseDto(schedule);
    }

    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto scheduleRequestDto) {
        Schedule schedule = new Schedule(scheduleRequestDto.getTodo(), scheduleRequestDto.getWriter(), scheduleRequestDto.getPassword());
        return scheduleRepository.saveSchedule(schedule);
    }

    @Override
    public void deleteSchedule(Long id, String password) {
        int deletedRow = scheduleRepository.deleteSchedule(id, password);
        if (deletedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, id + ": 아이디에 맞는 데이터가 없거나 비밀번호가 틀렸습니다.");
        }
    }

    @Override
    public ScheduleResponseDto updateSchedule(Long id, String todo, String writer, String password) {
        if (todo == null || writer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "할일과 작업자명은 필수입니다.");
        }

        int updatedRow = scheduleRepository.updateSchedule(id, todo, writer, password);
        if (updatedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, id + ": 아이디에 맞는 데이터가 없거나 비밀번호가 틀렸습니다.");
        }

        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);
        return null;
    }
}
