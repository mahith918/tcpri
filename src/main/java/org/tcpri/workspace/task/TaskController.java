package org.tcpri.workspace.task;

import org.tcpri.workspace.user.*;
import org.tcpri.workspace.notification.TaskAssignmentEmailService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskRepository tasks;
    private final UserRepository users;
    private final TaskAssignmentEmailService assignmentEmailService;

    public TaskController(TaskRepository tasks, UserRepository users, TaskAssignmentEmailService assignmentEmailService) {
        this.tasks = tasks; this.users = users; this.assignmentEmailService = assignmentEmailService;
    }

    @GetMapping
    public String list(Authentication auth, Model model) {
        boolean canSeeAll = auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));
        model.addAttribute("tasks", canSeeAll ? tasks.findAllByOrderByDueDateAsc()
                                              : tasks.findByAssignee_EmailIgnoreCaseOrderByDueDateAsc(auth.getName()));
        model.addAttribute("canAssign", canSeeAll);
        model.addAttribute("canDelete", auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        return "tasks";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String newTask(Model model) {
        model.addAttribute("form", new TaskForm());
        model.addAttribute("members", users.findAllByOrderByCreatedAtDesc().stream()
            .filter(u -> u.getStatus() == AccountStatus.APPROVED).toList());
        return "task-form";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String create(@Valid @ModelAttribute("form") TaskForm form,
                         org.springframework.validation.BindingResult result,
                         Authentication auth, Model model, RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("members", users.findAllByOrderByCreatedAtDesc().stream()
                .filter(u -> u.getStatus() == AccountStatus.APPROVED).toList());
            return "task-form";
        }
        AppUser assignee = users.findById(form.getAssigneeId())
            .filter(u -> u.getStatus() == AccountStatus.APPROVED)
            .orElseThrow(() -> new IllegalArgumentException("Assignee must be an approved user."));
        AppUser creator = users.findByEmailIgnoreCase(auth.getName()).orElseThrow();
        TeamTask savedTask = tasks.save(new TeamTask(form.getTitle(), form.getDescription(), form.getDueDate(), assignee, creator));
        assignmentEmailService.sendAssignmentNotifications(savedTask);
        flash.addFlashAttribute("message", "Task assigned.");
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam TaskStatus status,
                               Authentication auth, RedirectAttributes flash) {
        TeamTask task = tasks.findById(id).orElseThrow();
        boolean manager = auth.getAuthorities().stream().anyMatch(a ->
            a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));
        if (!manager && !task.getAssignee().getEmail().equalsIgnoreCase(auth.getName())) {
            throw new org.springframework.security.access.AccessDeniedException("Not your task.");
        }
        task.setStatus(status);
        tasks.save(task);
        flash.addFlashAttribute("message", "Task status updated.");
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        if (!tasks.existsById(id)) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND, "Task not found.");
        }
        tasks.deleteById(id);
        flash.addFlashAttribute("message", "Task deleted.");
        return "redirect:/tasks";
    }
}
