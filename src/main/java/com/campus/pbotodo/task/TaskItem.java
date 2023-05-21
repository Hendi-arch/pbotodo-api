package com.campus.pbotodo.task;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "task_item")
public class TaskItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "title is required")
    private String title;

    private boolean done;

    @NotNull(message = "username is required")
    @Column(name = "user_id", length = 30, nullable = false)
    private String username;

    @Builder.Default
    @NotNull(message = "dueDate is required")
    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate = LocalDateTime.now();

    @NotNull(message = "notificationChannelId is required")
    @Column(name = "notification_channel_id", nullable = false)
    private String notificationChannelId;

    @Builder.Default
    @NotNull(message = "urgency is required")
    @Column(nullable = false, length = 25)
    private String urgency = TaskUrgency.NOT_IMPORTANT.value;

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

}
