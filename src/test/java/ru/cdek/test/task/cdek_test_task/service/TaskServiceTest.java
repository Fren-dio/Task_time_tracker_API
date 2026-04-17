package ru.cdek.test.task.cdek_test_task.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.cdek.test.task.cdek_test_task.dto.task.TaskCreateDto;
import ru.cdek.test.task.cdek_test_task.dto.task.TaskResponseDto;
import ru.cdek.test.task.cdek_test_task.mapper.TaskMapper;
import ru.cdek.test.task.cdek_test_task.model.Task;
import ru.cdek.test.task.cdek_test_task.model.TaskStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_ShouldSaveAndReturnDto() {
        // given
        TaskCreateDto dto = new TaskCreateDto("Test Task", "Test Description");
        Task taskToInsert = new Task();
        taskToInsert.setTitle(dto.title());
        taskToInsert.setDescription(dto.description());
        taskToInsert.setStatus(TaskStatus.NEW);

        // когда вызывается insert, мы вручную устанавливаем ID (имитируем генерацию БД)
        doAnswer(invocation -> {
            Task t = invocation.getArgument(0);
            t.setId(1L);
            return null;
        }).when(taskMapper).insert(any(Task.class));

        // when
        TaskResponseDto result = taskService.createTask(dto);

        // then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.title()).isEqualTo("Test Task");
        assertThat(result.status()).isEqualTo(TaskStatus.NEW);
        verify(taskMapper, times(1)).insert(any(Task.class));
    }

    @Test
    void getTaskById_WhenExists_ShouldReturnDto() {
        // given
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Existing Task");
        task.setDescription("Desc");
        task.setStatus(TaskStatus.IN_PROGRESS);
        when(taskMapper.findById(1L)).thenReturn(task);

        // when
        TaskResponseDto result = taskService.getTaskById(1L);

        // then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.title()).isEqualTo("Existing Task");
        verify(taskMapper, times(1)).findById(1L);
    }

    @Test
    void getTaskById_WhenNotFound_ShouldThrowRuntimeException() {
        when(taskMapper.findById(99L)).thenReturn(null);
        assertThatThrownBy(() -> taskService.getTaskById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Задача не найдена");
    }

    @Test
    void updateTaskStatus_ShouldCallMapper() {
        taskService.updateTaskStatus(1L, TaskStatus.DONE);
        verify(taskMapper, times(1)).updateStatus(1L, TaskStatus.DONE);
    }
}