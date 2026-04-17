package ru.cdek.test.task.cdek_test_task.mapper;

import org.apache.ibatis.annotations.*;
import ru.cdek.test.task.cdek_test_task.model.TimeRecord;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TimeRecordMapper {

    @Insert("INSERT INTO time_records(employeeId, taskId, startTime, finishTime, taskDescription) " +
            "VALUES (#{employeeId}, #{taskId}, #{startTime}, #{finishTime}, #{taskDescription})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void createTimeRecord(TimeRecord timeRecord);

    @Select("SELECT * FROM time_records " +
            "WHERE employeeId = #{employeeId} AND startTime >= #{start} AND finishTime <= #{end} " +
            "ORDER BY startTime ASC")
    List<TimeRecord> findByEmployeeAndPeriod(@Param("employeeId") Long employeeId,
                                             @Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end);

}
