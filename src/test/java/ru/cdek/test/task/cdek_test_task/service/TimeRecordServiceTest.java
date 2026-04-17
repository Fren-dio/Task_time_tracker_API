package ru.cdek.test.task.cdek_test_task.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.cdek.test.task.cdek_test_task.mapper.TaskMapper;
import ru.cdek.test.task.cdek_test_task.mapper.TimeRecordMapper;
import ru.cdek.test.task.cdek_test_task.model.Task;
import ru.cdek.test.task.cdek_test_task.model.TimeRecord;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeRecordServiceTest {

    @Mock
    private TimeRecordMapper timeRecordMapper;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TimeRecordService timeRecordService;

    @Test
    void createTimeRecord_Valid_ShouldSave() {
        // given
        TimeRecord record = new TimeRecord();
        record.setTaskId(1L);
        record.setStartTime(LocalDateTime.now());
        record.setFinishTime(LocalDateTime.now().plusHours(2));

        when(taskMapper.findById(1L)).thenReturn(new Task()); // задача существует

        // when
        timeRecordService.createTimeRecord(record);

        // then
        verify(timeRecordMapper, times(1)).createTimeRecord(record);
    }

    @Test
    void createTimeRecord_WhenStartAfterFinish_ShouldThrowException() {
        TimeRecord record = new TimeRecord();
        record.setStartTime(LocalDateTime.now().plusHours(2));
        record.setFinishTime(LocalDateTime.now());

        assertThatThrownBy(() -> timeRecordService.createTimeRecord(record))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Время начала не может быть позже времени окончания");

        verify(timeRecordMapper, never()).createTimeRecord(any());
    }

    @Test
    void createTimeRecord_WhenTaskNotFound_ShouldThrowException() {
        TimeRecord record = new TimeRecord();
        record.setTaskId(99L);
        record.setStartTime(LocalDateTime.now());
        record.setFinishTime(LocalDateTime.now().plusHours(1));

        when(taskMapper.findById(99L)).thenReturn(null);

        assertThatThrownBy(() -> timeRecordService.createTimeRecord(record))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Task not found");

        verify(timeRecordMapper, never()).createTimeRecord(any());
    }

    @Test
    void findByEmployeeAndPeriod_ShouldReturnList() {
        Long employeeId = 1L;
        LocalDateTime start = LocalDateTime.of(2025, 4, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 4, 30, 23, 59);
        List<TimeRecord> mockRecords = List.of(new TimeRecord(), new TimeRecord());

        when(timeRecordMapper.findByEmployeeAndPeriod(employeeId, start, end)).thenReturn(mockRecords);

        List<TimeRecord> result = timeRecordService.findByEmployeeAndPeriod(employeeId, start, end);

        assertThat(result).hasSize(2);
        verify(timeRecordMapper, times(1)).findByEmployeeAndPeriod(employeeId, start, end);
    }
}