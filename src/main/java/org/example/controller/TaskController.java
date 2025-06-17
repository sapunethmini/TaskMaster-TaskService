package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.Priority;
import org.example.dto.TaskRequestDto;
import org.example.dto.TaskResponseDto;
import org.example.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task Management API", description = "Endpoints for creating, retrieving, updating, and deleting tasks.")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Create a new task", description = "Creates a new task and returns the created task details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input data")
    })
    @PostMapping("/createTask")
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody TaskRequestDto taskDto) {
        if (taskDto.getUserId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        TaskResponseDto createdTask = taskService.createTask(taskDto);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a task by ID", description = "Retrieves a single task by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(
            @Parameter(description = "The ID of the task to retrieve.", required = true) @PathVariable Long id) {
        TaskResponseDto task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @Operation(summary = "Get all tasks", description = "Retrieves a list of all tasks.")
    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        List<TaskResponseDto> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Update an existing task", description = "Updates the details of an existing task by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(
            @Parameter(description = "The ID of the task to update.", required = true) @PathVariable Long id,
            @RequestBody TaskRequestDto taskDto) {
        TaskResponseDto updatedTask = taskService.updateTask(id, taskDto);
        return ResponseEntity.ok(updatedTask);
    }

    @Operation(summary = "Delete a task by ID", description = "Deletes a task by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "The ID of the task to delete.", required = true) @PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get tasks by User ID", description = "Retrieves all tasks assigned to a specific user.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByUserId(
            @Parameter(description = "The ID of the user.", required = true) @PathVariable Long userId) {
        List<TaskResponseDto> tasks = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Get tasks by category", description = "Retrieves all tasks belonging to a specific category.")
    @GetMapping("/category/{category}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByCategory(
            @Parameter(description = "The category name to filter by.", required = true, example = "Work") @PathVariable String category) {
        List<TaskResponseDto> tasks = taskService.getTasksByCategory(category);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Get tasks by status", description = "Retrieves all tasks with a specific status.")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByStatus(
            @Parameter(description = "The status to filter by.", required = true, example = "IN_PROGRESS") @PathVariable String status) {
        List<TaskResponseDto> tasks = taskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Get tasks by User ID and Status", description = "Retrieves tasks for a specific user, filtered by status.")
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByUserIdAndStatus(
            @Parameter(description = "The ID of the user.", required = true) @PathVariable Long userId,
            @Parameter(description = "The status to filter by.", required = true) @PathVariable String status) {
        List<TaskResponseDto> tasks = taskService.getTasksByUserIdAndCategory(userId, status);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Sort all tasks by created date (descending)", description = "Retrieves all tasks, sorted with the newest first.")
    @GetMapping("/sort/created-date/desc")
    public ResponseEntity<List<TaskResponseDto>> getAllTasksSortedByCreatedDateDesc() {
        List<TaskResponseDto> tasks = taskService.getAllTasksSortedByCreatedDateDesc();
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Sort all tasks by created date (ascending)", description = "Retrieves all tasks, sorted with the oldest first.")
    @GetMapping("/sort/created-date/asc")
    public ResponseEntity<List<TaskResponseDto>> getAllTasksSortedByCreatedDateAsc() {
        List<TaskResponseDto> tasks = taskService.getAllTasksSortedByCreatedDateAsc();
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Sort all tasks by priority (descending)", description = "Retrieves all tasks, sorted from highest to lowest priority.")
    @GetMapping("/sort/priority/desc")
    public ResponseEntity<List<TaskResponseDto>> getAllTasksSortedByPriorityDesc() {
        List<TaskResponseDto> tasks = taskService.getAllTasksSortedByPriorityDesc();
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Sort all tasks by priority (ascending)", description = "Retrieves all tasks, sorted from lowest to highest priority.")
    @GetMapping("/sort/priority/asc")
    public ResponseEntity<List<TaskResponseDto>> getAllTasksSortedByPriorityAsc() {
        List<TaskResponseDto> tasks = taskService.getAllTasksSortedByPriorityAsc();
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Sort all tasks by title", description = "Retrieves all tasks, sorted alphabetically by title.")
    @GetMapping("/sort/title")
    public ResponseEntity<List<TaskResponseDto>> getAllTasksSortedByTitleAsc() {
        List<TaskResponseDto> tasks = taskService.getAllTasksSortedByTitleAsc();
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Sort all tasks by status", description = "Retrieves all tasks, sorted alphabetically by status.")
    @GetMapping("/sort/status")
    public ResponseEntity<List<TaskResponseDto>> getAllTasksSortedByStatusAsc() {
        List<TaskResponseDto> tasks = taskService.getAllTasksSortedByStatusAsc();
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Get tasks for a user, sorted by priority", description = "Retrieves tasks for a specific user, sorted by priority.")
    @GetMapping("/user/{userId}/sort/priority")
    public ResponseEntity<List<TaskResponseDto>> getTasksByUserIdSortedByPriority(
            @Parameter(description = "The ID of the user.", required = true) @PathVariable Long userId) {
        List<TaskResponseDto> tasks = taskService.getTasksByUserIdSortedByPriority(userId);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Get tasks for a user, sorted by created date", description = "Retrieves tasks for a specific user, sorted by creation date.")
    @GetMapping("/user/{userId}/sort/created-date")
    public ResponseEntity<List<TaskResponseDto>> getTasksByUserIdSortedByCreatedDate(
            @Parameter(description = "The ID of the user.", required = true) @PathVariable Long userId) {
        List<TaskResponseDto> tasks = taskService.getTasksByUserIdSortedByCreatedDate(userId);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Get tasks by category, sorted by priority", description = "Retrieves tasks in a category, sorted by priority.")
    @GetMapping("/category/{category}/sort/priority")
    public ResponseEntity<List<TaskResponseDto>> getTasksByCategorySortedByPriority(
            @Parameter(description = "The category name.", required = true) @PathVariable String category) {
        List<TaskResponseDto> tasks = taskService.getTasksByCategorySortedByPriority(category);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Get tasks by status, sorted by created date", description = "Retrieves tasks with a specific status, sorted by creation date.")
    @GetMapping("/status/{status}/sort/created-date")
    public ResponseEntity<List<TaskResponseDto>> getTasksByStatusSortedByCreatedDate(
            @Parameter(description = "The status to filter by.", required = true) @PathVariable String status) {
        List<TaskResponseDto> tasks = taskService.getTasksByStatusSortedByCreatedDate(status);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Get all tasks with dynamic sorting", description = "Retrieves all tasks, sorted by a specified field and direction.")
    @GetMapping("/sort")
    public ResponseEntity<List<TaskResponseDto>> getAllTasksSorted(
            @Parameter(description = "Field to sort by. (e.g., 'createdAt', 'priority', 'title')", schema = @Schema(defaultValue = "createdAt")) @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction. 'asc' for ascending, 'desc' for descending.", schema = @Schema(defaultValue = "desc")) @RequestParam(defaultValue = "desc") String direction) {
        List<TaskResponseDto> tasks = taskService.getAllTasksSorted(sortBy, direction);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Get task count by status", description = "Retrieves the total number of tasks for a given status.")
    @GetMapping("/status/{status}/count")
    public ResponseEntity<Long> getTaskCountByStatus(
            @Parameter(description = "The status to count.", required = true) @PathVariable String status) {
        Long count = taskService.getTaskCountByStatus(status);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Get task count by team", description = "Retrieves the total number of tasks for a given team.")
    @GetMapping("/team/{team}/count")
    public ResponseEntity<Long> getTaskCountByTeam(
            @Parameter(description = "The team name to count tasks for.", required = true) @PathVariable String team) {
        Long count = taskService.getTaskCountByTeam(team);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Get total task count", description = "Retrieves the total number of tasks in the system.")
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalTaskCount() {
        Long count = taskService.getTotalTaskCount();
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Get tasks by team and status", description = "Retrieves tasks assigned to a specific team and filtered by status.")
    @GetMapping("/team/{team}/status/{status}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByTeamAndStatus(
            @Parameter(description = "The team name.", required = true) @PathVariable String team,
            @Parameter(description = "The status to filter by.", required = true) @PathVariable String status) {
        List<TaskResponseDto> tasks = taskService.getTasksByTeamAndStatus(team, status);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Get task count by team and status", description = "Retrieves the number of tasks for a specific team and status.")
    @GetMapping("/team/{team}/status/{status}/count")
    public ResponseEntity<Long> getTaskCountByTeamAndStatus(
            @Parameter(description = "The team name.", required = true) @PathVariable String team,
            @Parameter(description = "The status to count.", required = true) @PathVariable String status) {
        Long count = taskService.getTaskCountByTeamAndStatus(team, status);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Get task count by team and priority", description = "Retrieves the number of tasks for a specific team and priority level.")
    @GetMapping("/team/{team}/priority/{priority}/count")
    public ResponseEntity<Long> getTaskCountByTeamAndPriority(
            @Parameter(description = "The team name.", required = true) @PathVariable String team,
            @Parameter(description = "The priority level (HIGH, MEDIUM, LOW).", required = true) @PathVariable Priority priority) {
        Long count = taskService.getTaskCountByTeamAndPriority(team, priority);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Get tasks by team and priority", description = "Retrieves tasks for a specific team, filtered by priority level.")
    @GetMapping("/team/{team}/priority/{priority}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByTeamAndPriority(
            @Parameter(description = "The team name.", required = true) @PathVariable String team,
            @Parameter(description = "The priority level (HIGH, MEDIUM, LOW).", required = true) @PathVariable Priority priority) {
        List<TaskResponseDto> tasks = taskService.getTasksByTeamAndPriority(team, priority);
        return ResponseEntity.ok(tasks);
    }
}