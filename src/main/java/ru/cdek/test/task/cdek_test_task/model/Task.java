package ru.cdek.test.task.cdek_test_task.model;

import lombok.Data;

@Data
public class Task {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
}
