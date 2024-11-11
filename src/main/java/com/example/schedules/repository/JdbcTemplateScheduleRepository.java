package com.example.schedules.repository;

import com.example.schedules.dto.ScheduleResponseDto;
import com.example.schedules.entity.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcTemplateScheduleRepository implements ScheduleRepository{
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateScheduleRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<ScheduleResponseDto> findAll(String writer, String updatedAt) {

        StringBuilder query = new StringBuilder("SELECT * FROM schedule WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (writer != null) {
            query.append(" AND writer = ?");
            params.add(writer);
        }

        if (updatedAt != null) {
            query.append(" AND DATE_FORMAT(updatedAt, '%Y-%m-%d') = ?");
            params.add(updatedAt);
        }

        query.append(" ORDER BY modified_at DESC;");

        return jdbcTemplate.query(query.toString(), params.toArray(), scheduleRowMapper());
    }

    @Override
    public Schedule findScheduleByIdOrElseThrow(Long id) {
        List<Schedule> result = jdbcTemplate.query("SELECT * FROM schedule WHERE id = ?", scheduleRowMapperV2(), id);
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, id + ": 아이디에 맞는 데이터가 없습니다."));
    }

    @Override
    public ScheduleResponseDto saveSchedule(Schedule schedule) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("schedule").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("todo", schedule.getTodo());
        parameters.put("writer", schedule.getWriter());
        parameters.put("password", schedule.getPassword());
        parameters.put("createdAt", schedule.getCreatedAt());
        parameters.put("updatedAt", schedule.getCreatedAt());

        Number key = simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        return new ScheduleResponseDto(key.longValue(), schedule.getTodo(), schedule.getWriter(), schedule.getCreatedAt(), schedule.getUpdatedAt());
    }

    @Override
    public int deleteSchedule(Long id, String password) {
        return jdbcTemplate.update("DELETE FROM schedule WHERE id = ? AND password = ?", id, password);
    }

    @Override
    public int updateSchedule(Long id, String todo, String writer, String password) {
        return jdbcTemplate.update("UPDATE schedule SET todo = ?, writer = ?, updatedAt = ? AND PASSWORD = ?", todo, writer, LocalDateTime.now(), id, password);
    }

    private RowMapper<ScheduleResponseDto> scheduleRowMapper() {
        return new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ScheduleResponseDto(
                        rs.getLong("id"),
                        rs.getString("todo"),
                        rs.getString("writer"),
                        rs.getObject("createdAt", LocalDateTime.class),
                        rs.getObject("updatedAt", LocalDateTime.class)
                );
            }
        };
    }

    private RowMapper<Schedule> scheduleRowMapperV2() {
        return new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Schedule(
                        rs.getLong("id"),
                        rs.getString("todo"),
                        rs.getString("writer"),
                        rs.getObject("createdAt", LocalDateTime.class),
                        rs.getObject("updatedAt", LocalDateTime.class)
                );
            }
        };
    }
}
