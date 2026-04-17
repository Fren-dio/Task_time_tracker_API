package ru.cdek.test.task.cdek_test_task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cdek.test.task.cdek_test_task.dto.task.TaskCreateDto;
import ru.cdek.test.task.cdek_test_task.dto.task.TaskResponseDto;
import ru.cdek.test.task.cdek_test_task.dto.task.TaskStatusUpdateDto;
import ru.cdek.test.task.cdek_test_task.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Управление задачами")
public class TaskController {
    private final TaskService taskService;


    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping()
    @Operation(summary = "Создать задачу", description = "Создаёт новую задачу со статусом NEW")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Задача успешно создана",
                    content = @Content(schema = @Schema(implementation = TaskResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные (например, пустое название)"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskCreateDto dto) {
        TaskResponseDto task = taskService.createTask(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить задачу по ID", description = "Возвращает полную информацию о задаче")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача найдена",
                    content = @Content(schema = @Schema(implementation = TaskResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Задача с указанным ID не найдена"),
            @ApiResponse(responseCode = "400", description = "Неверный формат ID")
    })
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable Long id) {
        TaskResponseDto task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Изменить статус задачи", description = "Обновляет статус задачи (NEW, IN_PROGRESS, DONE)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Статус успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Неверный статус или ID"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    public ResponseEntity<Void> updateTaskStatus(@PathVariable Long id, @Valid @RequestBody TaskStatusUpdateDto dto) {
        taskService.updateTaskStatus(id, dto.status());
        return ResponseEntity.noContent().build();
    }
}
