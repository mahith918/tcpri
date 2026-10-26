package org.tcpri.workspace.task;

import org.tcpri.workspace.user.AppUser;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class TeamTask {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 180)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(name = "due_date")
    private LocalDate dueDate;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private TaskStatus status = TaskStatus.TODO;
    @ManyToOne(optional = false) @JoinColumn(name = "assignee_id")
    private AppUser assignee;
    @ManyToOne(optional = false) @JoinColumn(name = "created_by_id")
    private AppUser createdBy;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    protected TeamTask() {}
    public TeamTask(String title, String description, LocalDate dueDate, AppUser assignee, AppUser createdBy) {
        this.title = title; this.description = description; this.dueDate = dueDate;
        this.assignee = assignee; this.createdBy = createdBy;
    }
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDueDate() { return dueDate; }
    public TaskStatus getStatus() { return status; }
    public AppUser getAssignee() { return assignee; }
    public AppUser getCreatedBy() { return createdBy; }
    public void setStatus(TaskStatus status) { this.status = status; }
}
