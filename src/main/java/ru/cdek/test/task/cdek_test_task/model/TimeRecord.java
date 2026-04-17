package ru.cdek.test.task.cdek_test_task.model;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class TimeRecord {
    private Long id;
    private Long employeeId;
    private Long taskId;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private String taskDescription;
}
