package ru.cdek.test.task.cdek_test_task.mapper;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.cdek.test.task.cdek_test_task.model.Task;
import ru.cdek.test.task.cdek_test_task.model.TaskStatus;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskMapperIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TaskMapper taskMapper;

    // проверка, что запись корректно создалась (с не null id), и что она читается
    @Test
    void shouldInsertAndFindTask() {
        Task task = new Task();
        task.setTitle("Integration Task");
        task.setDescription("Desc");
        task.setStatus(TaskStatus.NEW);

        taskMapper.insert(task);
        assertThat(task.getId()).isNotNull();

        Task found = taskMapper.findById(task.getId());
        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("Integration Task");
        assertThat(found.getStatus()).isEqualTo(TaskStatus.NEW);
    }

    // проверка, что статус задачи корректно меняется
    @Test
    void shouldUpdateStatus() {
        Task task = new Task();
        task.setTitle("To be updated");
        task.setStatus(TaskStatus.NEW);
        taskMapper.insert(task);
        Long taskId = task.getId();

        taskMapper.updateStatus(taskId, TaskStatus.DONE);

        Task updatedTask = taskMapper.findById(taskId);
        assertThat(updatedTask.getStatus()).isEqualTo(TaskStatus.DONE);
    }
}