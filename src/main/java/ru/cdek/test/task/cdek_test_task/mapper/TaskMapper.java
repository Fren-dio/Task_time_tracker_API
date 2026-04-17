package ru.cdek.test.task.cdek_test_task.mapper;

import org.apache.ibatis.annotations.*;
import ru.cdek.test.task.cdek_test_task.model.Task;
import ru.cdek.test.task.cdek_test_task.model.TaskStatus;

@Mapper
public interface TaskMapper {

    @Insert("INSERT INTO tasks(title, description, status) " +
            "VALUES (#{title}, #{description}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Task task);

    @Select("SELECT id, title, description, status " +
            "FROM tasks " +
            "WHERE id = #{id}")
    Task findById(Long id);

    @Update("UPDATE tasks " +
            "SET status = #{status} " +
            "WHERE id = #{id}")
    void updateStatus(Long id, TaskStatus status);

}
