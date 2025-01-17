package org.example.tms.repository;

import org.example.tms.model.Comment;
import org.example.tms.model.Task;
import org.example.tms.model.TaskAssignee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    @Query("SELECT ta FROM TaskAssignee ta WHERE ta.task.id = :taskId")
    List<TaskAssignee> fetchTaskWithTaskAssignees(@Param("taskId") UUID taskId);

    @Query("SELECT c FROM Comment c WHERE c.task.id = :taskId")
    List<Comment> fetchTaskWithComments(@Param("taskId") UUID taskId);

    @EntityGraph(attributePaths = {"author"})
    @Query("SELECT DISTINCT t FROM Task t WHERE t.author.id = :authorId")
    Page<Task> findAllByAuthor_Id(@Param("authorId") UUID authorId, Pageable pageable);

    @EntityGraph(attributePaths = {"author"})
    @Query("SELECT DISTINCT t FROM Task t JOIN t.taskAssignees ta WHERE ta.assignee.id = :assigneeId")
    Page<Task> findAllByTaskAssignees_Assignee_Id(@Param("assigneeId") UUID assigneeId, Pageable pageable);

    @EntityGraph(attributePaths = {"author"})
    @Query("SELECT DISTINCT t FROM Task t")
    Page<Task> findAllWithDetails(Pageable pageable);
}
