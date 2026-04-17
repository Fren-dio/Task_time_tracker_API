package ru.cdek.test.task.cdek_test_task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cdek.test.task.cdek_test_task.dto.timeRecord.TimeRecordCreateDto;
import ru.cdek.test.task.cdek_test_task.dto.timeRecord.TimeRecordResponseDto;
import ru.cdek.test.task.cdek_test_task.model.TimeRecord;
import ru.cdek.test.task.cdek_test_task.service.TimeRecordService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/time-record")
@Tag(name = "Time Records", description = "Учёт рабочего времени")
public class TimeRecordController {

    private final TimeRecordService timeRecordService;

    public TimeRecordController(TimeRecordService timeRecordService) {
        this.timeRecordService = timeRecordService;
    }

    @PostMapping
    @Operation(summary = "Зафиксировать время", description = "Создаёт запись о затраченном времени на задачу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Запись успешно создана",
                    content = @Content(schema = @Schema(implementation = TimeRecordResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Неверные данные (например, время начала позже окончания, задача не найдена)"),
            @ApiResponse(responseCode = "404", description = "Указанная задача не существует")
    })
    public ResponseEntity<TimeRecordResponseDto> createTimeRecord(@Valid @RequestBody TimeRecordCreateDto dto) {

        TimeRecord record = new TimeRecord();
        record.setEmployeeId(dto.employeeId());
        record.setTaskId(dto.taskId());
        record.setStartTime(dto.startTime());
        record.setFinishTime(dto.endTime());
        record.setTaskDescription(dto.workDescription());

        timeRecordService.createTimeRecord(record);

        TimeRecordResponseDto response = new TimeRecordResponseDto(record.getId(), record.getEmployeeId(), record.getTaskId(),
                                                record.getStartTime(), record.getFinishTime(), record.getTaskDescription()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Получить информацию о затраченном времени сотрудника за период
    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Получить отчёт по сотруднику за период",
            description = "Возвращает список всех записей времени для указанного сотрудника в заданном временном интервале")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список записей (может быть пустым)",
                    content = @Content(schema = @Schema(implementation = TimeRecordResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Неверные параметры (например, start > end)")
    })
    public ResponseEntity<List<TimeRecordResponseDto>> getRecordsByPeriod(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        List<TimeRecord> records = timeRecordService.findByEmployeeAndPeriod(employeeId, start, end);

        List<TimeRecordResponseDto> responseDtos = records.stream()
                .map(r -> new TimeRecordResponseDto(
                        r.getId(),
                        r.getEmployeeId(),
                        r.getTaskId(),
                        r.getStartTime(),
                        r.getFinishTime(),
                        r.getTaskDescription()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }
}
