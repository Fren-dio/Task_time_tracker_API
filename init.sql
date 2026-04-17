-- init.sql
CREATE TABLE IF NOT EXISTS tasks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL
    );

CREATE TABLE IF NOT EXISTS time_records (
    id BIGSERIAL PRIMARY KEY,
    employeeId BIGINT NOT NULL,
    taskId BIGINT NOT NULL,
    startTime TIMESTAMP NOT NULL,
    finishTime TIMESTAMP NOT NULL,
    taskDescription TEXT,
    FOREIGN KEY (taskId) REFERENCES tasks(id) ON DELETE CASCADE
);