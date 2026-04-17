package ru.cdek.test.task.cdek_test_task.dto.task;

import jakarta.validation.constraints.NotNull;
import ru.cdek.test.task.cdek_test_task.model.TaskStatus;

public record TaskStatusUpdateDto(
        @NotNull(message = "Статус обязателен")
        TaskStatus status
) {
}
