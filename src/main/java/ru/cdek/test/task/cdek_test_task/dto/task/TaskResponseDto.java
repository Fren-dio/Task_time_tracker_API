package ru.cdek.test.task.cdek_test_task.dto.task;

import jakarta.validation.constraints.NotNull;
import ru.cdek.test.task.cdek_test_task.model.TaskStatus;

public record TaskResponseDto(
        @NotNull(message = "ID сотрудника обязателен")
        Long id,
        @NotNull(message = "ID задачи обязателен")
        String title,
        @NotNull(message = "Время начала обязательно")
        String description,
        @NotNull(message = "Время окончания обязательно")
        TaskStatus status
) {
}
