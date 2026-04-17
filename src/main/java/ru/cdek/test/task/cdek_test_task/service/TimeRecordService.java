package ru.cdek.test.task.cdek_test_task.service;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import ru.cdek.test.task.cdek_test_task.mapper.TaskMapper;
import ru.cdek.test.task.cdek_test_task.mapper.TimeRecordMapper;
import ru.cdek.test.task.cdek_test_task.model.Task;
import ru.cdek.test.task.cdek_test_task.model.TimeRecord;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TimeRecordService {

    private final TimeRecordMapper timeRecordMapper;
    private final TaskMapper taskMapper;

    public TimeRecordService(TimeRecordMapper timeRecordMapper, TaskMapper taskMapper) {
        this.timeRecordMapper = timeRecordMapper;
        this.taskMapper = taskMapper;
    }

    public void createTimeRecord(TimeRecord timeRecord) {
        // validate
        if (timeRecord.getStartTime().isAfter(timeRecord.getFinishTime())) {
            throw new IllegalArgumentException("Время начала не может быть позже времени окончания");
        }

        Task task = taskMapper.findById(timeRecord.getTaskId());
        if (task == null) {
            throw new RuntimeException("Task not found");
        }

        timeRecordMapper.createTimeRecord(timeRecord);
    }

    public List<TimeRecord> findByEmployeeAndPeriod(Long employeeId, LocalDateTime start, LocalDateTime end) {
        return timeRecordMapper.findByEmployeeAndPeriod(employeeId, start, end);
    }
}
