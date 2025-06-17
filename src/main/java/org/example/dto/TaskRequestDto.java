package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Data Transfer Object for creating or updating a task.")
public class TaskRequestDto {

    @Schema(description = "The title of the task.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Design New Homepage")
    private String title;

    @Schema(description = "A detailed description of the task.", example = "Create mockups and a prototype for the new company homepage.")
    private String description;

    @Schema(description = "The current status of the task.", example = "TODO")
    private String status;

    @Schema(description = "The category the task belongs to.", example = "Design")
    private String category;

    @Schema(description = "The team assigned to the task.", example = "Marketing")
    private String team;

    @Schema(description = "The priority level of the task.", example = "MEDIUM")
    private Priority priority;

    @JsonProperty("userId")
    @Schema(description = "The ID of the user to whom the task will be assigned.", requiredMode = Schema.RequiredMode.REQUIRED, example = "205")
    private Long userId;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Schema(description = "The due date for the task. Use ISO format.", example = "2025-07-15T18:00:00")
    private LocalDateTime dueDate;

    @Schema(description = "Estimated duration to complete the task, in minutes.", example = "180")
    private Integer duration;


    @Override
    public String toString() {
        return "TaskRequestDto{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", category='" + category + '\'' +
                ", team='" + team + '\'' +
                ", priority=" + priority +
                ", userId=" + userId +
                ", dueDate=" + dueDate +
                ", duration=" + duration +
                '}';
    }
}