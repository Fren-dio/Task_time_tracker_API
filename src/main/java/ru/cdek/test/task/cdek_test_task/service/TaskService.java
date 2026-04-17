package ru.cdek.test.task.cdek_test_task.service;

import org.springframework.stereotype.Service;
import ru.cdek.test.task.cdek_test_task.dto.task.TaskCreateDto;
import ru.cdek.test.task.cdek_test_task.dto.task.TaskResponseDto;
import ru.cdek.test.task.cdek_test_task.exception.exceptions.ResourceNotFoundException;
import ru.cdek.test.task.cdek_test_task.mapper.TaskMapper;
import ru.cdek.test.task.cdek_test_task.model.Task;
import ru.cdek.test.task.cdek_test_task.model.TaskStatus;

@Service
public class TaskService {

    private final TaskMapper taskMapper;

    public TaskService(TaskMapper taskMapper) {this.taskMapper = taskMapper;}

    public TaskResponseDto createTask(TaskCreateDto taskDto) {
        Task task = new Task();
        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());
        task.setStatus(TaskStatus.NEW);

        taskMapper.insert(task);

        return new TaskResponseDto(task.getId(), task.getTitle(), task.getDescription(), task.getStatus());
    }

    public TaskResponseDto getTaskById(Long id) {

        Task task = taskMapper.findById(id);
        if (task == null) {
            throw new ResourceNotFoundException("Задача не найдена");
        }

        return new TaskResponseDto(task.getId(), task.getTitle(), task.getDescription(), task.getStatus());
    }

    public void updateTaskStatus(Long id, TaskStatus newStatus) {
        taskMapper.updateStatus(id, newStatus);
    }
}
