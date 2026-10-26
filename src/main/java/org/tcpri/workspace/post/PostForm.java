package org.tcpri.workspace.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class PostForm {
    @NotBlank @Size(max = 180)
    private String title;
    @Size(max = 10000)
    private String caption;
    @NotNull
    private SocialPlatform platform;
    private LocalDateTime plannedAt;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
    public SocialPlatform getPlatform() { return platform; }
    public void setPlatform(SocialPlatform platform) { this.platform = platform; }
    public LocalDateTime getPlannedAt() { return plannedAt; }
    public void setPlannedAt(LocalDateTime plannedAt) { this.plannedAt = plannedAt; }
}
