package ru.cdek.test.task.cdek_test_task.mapper;

import org.junit.jupiter.api.BeforeEach;
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
import ru.cdek.test.task.cdek_test_task.model.TimeRecord;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TimeRecordMapperIT {

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

    @Autowired
    private TimeRecordMapper timeRecordMapper;

    private Long existingTaskId;

    @BeforeEach
    void setUp() {
        Task task = new Task();
        task.setTitle("Task for records");
        task.setStatus(TaskStatus.NEW);
        taskMapper.insert(task);
        existingTaskId = task.getId();
    }

    // проверка, что timereport создается корректно с не null id и что при поиске найденная запись совпадает с добавленной
    @Test
    void shouldInsertAndFindByEmployeeAndPeriod() {
        TimeRecord record = new TimeRecord();
        record.setEmployeeId(100L);
        record.setTaskId(existingTaskId);
        record.setStartTime(LocalDateTime.of(2025, 4, 17, 10, 0));
        record.setFinishTime(LocalDateTime.of(2025, 4, 17, 12, 0));
        record.setTaskDescription("Coding");

        timeRecordMapper.createTimeRecord(record);
        assertThat(record.getId()).isNotNull();

        List<TimeRecord> records = timeRecordMapper.findByEmployeeAndPeriod(
                100L,
                LocalDateTime.of(2025, 4, 1, 0, 0),
                LocalDateTime.of(2025, 4, 30, 23, 59)
        );

        assertThat(records).hasSize(1);
        assertThat(records.get(0).getTaskDescription()).isEqualTo("Coding");
    }
}