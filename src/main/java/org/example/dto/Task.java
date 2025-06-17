package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Represents the core attributes of a task.")
public class Task {

    @Schema(description = "The title of the task.", example = "Plan Team Offsite")
    private String title;

    @Schema(description = "A detailed description of the task.", example = "Organize logistics for the annual team offsite event.")
    private String description;

    @Schema(description = "The current status of the task.", example = "COMPLETED")
    private String status;

    @Schema(description = "The category the task belongs to.", example = "HR")
    private String category;

    @Schema(description = "The priority level of the task.", example = "LOW")
    private Priority priority;

    @Schema(description = "The ID of the user associated with the task.", example = "301")
    private Long userId;

    @Schema(description = "Estimated duration to complete the task, in minutes.", example = "480")
    private Integer duration;

    @Schema(description = "The due date for the task.", example = "2025-08-20T12:00:00")
    private LocalDateTime dueDate;

    @Schema(description = "The team assigned to the task.", example = "Management")
    private String team;
}