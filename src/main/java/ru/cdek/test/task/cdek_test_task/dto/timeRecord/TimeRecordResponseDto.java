package ru.cdek.test.task.cdek_test_task.dto.timeRecord;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TimeRecordResponseDto(
        @NotNull(message = "ID обязателен")
        Long id,

        @NotNull(message = "ID сотрудника обязателен")
        Long employeeId,

        @NotNull(message = "ID задачи обязательно")
        Long taskId,

        @NotNull(message = "Время начала обязательно")
        LocalDateTime startTime,

        @NotNull(message = "Время окончания обязательно")
        LocalDateTime endTime,

        @Size(max = 2500, message = "Описание слишком длинное. Максимум 2500 символов")
        String workDescription
) {
}
