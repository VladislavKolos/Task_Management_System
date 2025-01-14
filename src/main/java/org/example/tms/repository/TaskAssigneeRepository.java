package org.example.tms.repository;

import org.example.tms.model.TaskAssignee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskAssigneeRepository extends JpaRepository<TaskAssignee, UUID> {
}
