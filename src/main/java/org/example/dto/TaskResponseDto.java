package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Data Transfer Object for sending task details in an API response.")
public class TaskResponseDto {

    @Schema(description = "The unique identifier of the task.", example = "1")
    private Long id;

    @Schema(description = "The title of the task.", example = "Finalize Q3 Report")
    private String title;

    @Schema(description = "A detailed description of the task.", example = "Compile all department data and generate the final Q3 financial report.")
    private String description;

    @Schema(description = "The current status of the task.", example = "IN_PROGRESS")
    private String status;

    @Schema(description = "The category the task belongs to.", example = "Finance")
    private String category;

    @Schema(description = "The team assigned to the task.", example = "Accounting")
    private String team;

    @Schema(description = "The priority level of the task.", example = "HIGH")
    private Priority priority;

    @Schema(description = "The ID of the user to whom the task is assigned.", example = "101")
    private Long userId;

    @Schema(description = "The due date for the task.", example = "2025-09-30T17:00:00")
    private LocalDateTime dueDate;

    @Schema(description = "Estimated duration to complete the task, in minutes.", example = "240")
    private Integer duration;

    @Schema(description = "The timestamp when the task was created.", example = "2025-06-17T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "The timestamp when the task was last updated.", example = "2025-06-17T11:30:00")
    private LocalDateTime updatedAt;
}