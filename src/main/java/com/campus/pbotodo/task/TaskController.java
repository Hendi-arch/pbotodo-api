package com.campus.pbotodo.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private TaskService taskService;

    @GetMapping("/get")
    public List<TaskItem> getTasks(@RequestParam String username) {
        return taskRepo.findByUsername(username);
    }

    @PostMapping("/add")
    public TaskItem addTask(@Valid @RequestBody TaskItem taskItem) {
        var task = taskRepo.save(taskItem);
        taskService.scheduleTaskReminder(task);
        return task;
    }

    @PostMapping("/update")
    public TaskItem updateTask(@Valid @RequestBody TaskItem taskItem) {
        TaskItem task = taskRepo.findByIdAndUsername(taskItem.getId(), taskItem.getUsername());
        task.setTitle(taskItem.getTitle());
        task.setDueDate(taskItem.getDueDate());
        task.setNotificationChannelId(taskItem.getNotificationChannelId());
        task.setUrgency(taskItem.getUrgency());
        task.setUpdatedAt(LocalDateTime.now());
        taskService.scheduleTaskReminder(task);
        return taskRepo.save(task);
    }

    @PutMapping("/update/{id}/{username}")
    public ResponseEntity<String> updateTaskProgress(@PathVariable Long id, @PathVariable String username) {
        boolean exist = taskRepo.existsByIdAndUsername(id, username);
        if (exist) {
            TaskItem task = taskRepo.findByIdAndUsername(id, username);
            task.setDone(!task.isDone());
            task.setUpdatedAt(LocalDateTime.now());
            task = taskRepo.save(task);

            if (task.isDone()) {
                taskService.removeScheduleTaskReminder(id, username);
            } else {
                taskService.scheduleTaskReminder(task);
            }
            return ResponseEntity.ok("Task is updated");
        }
        return new ResponseEntity<>("Task is not exist", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/delete/{id}/{username}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id, @PathVariable String username) {
        boolean exist = taskRepo.existsByIdAndUsername(id, username);
        if (exist) {
            taskRepo.deleteById(id);
            taskService.removeScheduleTaskReminder(id, username);
            return ResponseEntity.ok("Task is deleted");
        }
        return new ResponseEntity<>("Task is not exist", HttpStatus.BAD_REQUEST);
    }
}
