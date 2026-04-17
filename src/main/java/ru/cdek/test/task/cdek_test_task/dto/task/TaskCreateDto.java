package ru.cdek.test.task.cdek_test_task.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskCreateDto(
        @NotBlank(message = "Название задачи не может быть пустым")
        @Size(max = 255, message = "Название не длиннее 255 символов")
        String title,
        @Size(max = 2500, message = "Описание слишком длинное. Максимум 2500 символов")
        String description
) {
}
