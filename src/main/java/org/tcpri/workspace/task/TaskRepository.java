package org.tcpri.workspace.task;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<TeamTask, Long> {
    List<TeamTask> findByAssignee_EmailIgnoreCaseOrderByDueDateAsc(String email);
    List<TeamTask> findAllByOrderByDueDateAsc();
}
