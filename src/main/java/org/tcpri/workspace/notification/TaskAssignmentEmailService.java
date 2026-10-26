package org.tcpri.workspace.notification;

import org.tcpri.workspace.task.TeamTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class TaskAssignmentEmailService {
    private static final Logger log = LoggerFactory.getLogger(TaskAssignmentEmailService.class);

    private final JavaMailSender mailSender;
    private final String from;
    private final String adminEmail;
    private final String baseUrl;

    public TaskAssignmentEmailService(
            JavaMailSender mailSender,
            @Value("${app.mail.from:}") String from,
            @Value("${app.mail.admin:}") String adminEmail,
            @Value("${app.base-url:http://localhost:8080}") String baseUrl) {
        this.mailSender = mailSender;
        this.from = from;
        this.adminEmail = adminEmail;
        this.baseUrl = baseUrl;
    }

    public void sendAssignmentNotifications(TeamTask task) {
        if (from == null || from.isBlank()) {
            log.warn("Assignment email not sent: MAIL_FROM is not configured. Task id={}", task.getId());
            return;
        }

        String subject = "New work assigned: " + task.getTitle();
        String body = """
                Hello %s,

                A new task has been assigned to you.

                Task: %s
                Description: %s
                Due date: %s
                Status: %s

                Open TCPRI: %s/tasks

                Please sign in to view the task and update its status.
                """.formatted(
                    task.getAssignee().getName(),
                    task.getTitle(),
                    task.getDescription() == null || task.getDescription().isBlank()
                        ? "No additional description provided." : task.getDescription(),
                    task.getDueDate() == null ? "Not specified" : task.getDueDate(),
                    task.getStatus(),
                    baseUrl
                );

        send(task.getAssignee().getEmail(), subject, body);

        if (adminEmail != null && !adminEmail.isBlank()
                && !adminEmail.equalsIgnoreCase(task.getAssignee().getEmail())) {
            String adminBody = """
                    A task assignment has been created in TCPRI.

                    Task: %s
                    Assigned to: %s (%s)
                    Assigned by: %s
                    Due date: %s
                    Open task list: %s/tasks
                    """.formatted(
                        task.getTitle(),
                        task.getAssignee().getName(),
                        task.getAssignee().getEmail(),
                        task.getCreatedBy().getName(),
                        task.getDueDate() == null ? "Not specified" : task.getDueDate(),
                        baseUrl
                    );
            send(adminEmail, "Task assignment confirmation: " + task.getTitle(), adminBody);
        }
    }

    private void send(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Assignment notification email sent to {}", to);
        } catch (MailException ex) {
            // Email failure must not undo a task already saved in the database.
            log.error("Could not send assignment notification to {}. Configure SMTP and review mail logs.", to, ex);
        }
    }
}
