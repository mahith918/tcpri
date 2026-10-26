package org.tcpri.workspace.post;

import org.tcpri.workspace.user.AppUser;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "social_posts")
public class SocialPost {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 180)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String caption;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30)
    private SocialPlatform platform;
    @Column(name = "planned_at")
    private LocalDateTime plannedAt;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30)
    private PostStatus status = PostStatus.DRAFT;
    @ManyToOne(optional = false) @JoinColumn(name = "created_by_id")
    private AppUser createdBy;
    @ManyToOne @JoinColumn(name = "approved_by_id")
    private AppUser approvedBy;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    protected SocialPost() {}
    public SocialPost(String title, String caption, SocialPlatform platform, LocalDateTime plannedAt, AppUser createdBy) {
        this.title = title; this.caption = caption; this.platform = platform;
        this.plannedAt = plannedAt; this.createdBy = createdBy;
    }
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getCaption() { return caption; }
    public SocialPlatform getPlatform() { return platform; }
    public LocalDateTime getPlannedAt() { return plannedAt; }
    public PostStatus getStatus() { return status; }
    public AppUser getCreatedBy() { return createdBy; }
    public AppUser getApprovedBy() { return approvedBy; }
    public void setStatus(PostStatus status) { this.status = status; }
    public void setApprovedBy(AppUser approvedBy) { this.approvedBy = approvedBy; }
}
