package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.innoxpert.Entity.Task;
import tn.esprit.innoxpert.Entity.TypeStatus;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Service.TaskServiceInterface;

import java.util.List;
import java.util.Map;

@Tag(name = "Task Management")
@RestController
@AllArgsConstructor
@RequestMapping("/task")
public class TaskRestController {
    @Autowired
    TaskServiceInterface taskService;

    @GetMapping("/getAllTasks")
    public List<Task> getAllTasks()
    {
        return taskService.getAllTasks();
    }
    @GetMapping("/getTaskById/{idTask}")
    public Task getTaskById(@PathVariable("idTask") Long idTask)
    {
        return taskService.getTaskById(idTask);
    }
    @PostMapping("/addTask")
    public Task addTask ( @RequestBody Task Task)
    {
        return taskService.addTask(Task);
    }

    @DeleteMapping("/deleteTask/{idTask}")
    public void deleteTaskById(@PathVariable ("idTask") Long idTask)
    {
        taskService.removeTaskById(idTask);
    }

    @PutMapping("/updateTask")

    public Task updateTask(@RequestBody Task Task)
    {
        return taskService.updateTask(Task);
    }



    @PostMapping("/addTaskAndAssignToStudent/{idUser}")
    public Task addTaskAndAssignToStudent(@RequestBody Task task, @PathVariable("idUser") Long idUser) {
        return taskService.addAndaffectTaskToStudent(idUser, task);
    }
    @PutMapping("/updateAndAssignTaskToStudent/{idUser}")
    public Task updateAndAssignTaskToStudent( @RequestBody Task task,@PathVariable Long idUser) {
        return taskService.updateAndaffectTaskToStudent(idUser, task);
    }
    @PutMapping("/changeTaskStatus/{idTask}/{typeStatus}")
    public Task changeTaskStatus(@PathVariable("idTask") Long idTask, @PathVariable("typeStatus") TypeStatus typeStatus) {
        return taskService.ChangeTaskStatus(idTask, typeStatus);
    }


    @GetMapping("getTasksByUserId")
    public List<Task> getTasksByUserId(Long idUser)
    {
        return taskService.getTasksByUserId(idUser);
    }

    @GetMapping("findStudentWithMostDoneTasks")
    public User findStudentWithMostDoneTasks(){return taskService.findStudentWithMostDoneTasks();}


    @GetMapping("/done-tasks-count/{studentId}")
    public ResponseEntity<Integer> countDoneTasksByStudent(@PathVariable Long studentId) {
        int doneCount = taskService.countDoneTasksByStudent(studentId);
        return ResponseEntity.ok(doneCount);
    }


    @PutMapping("/rateTask/{taskId}/{mark}")
    public Task rateTask(@PathVariable Long taskId, @PathVariable Integer mark) {
        return taskService.rateTask(taskId, mark);
    }

    @PostMapping("/help-request/{taskId}")
    public ResponseEntity<String> sendHelpRequest(@PathVariable Long taskId, @RequestBody Map<String, String> body) {
        try {
            String messageContent = body.get("message");
            if (messageContent == null || messageContent.trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message content is required.");
            }

            taskService.sendHelpRequest(taskId, messageContent);
            return ResponseEntity.ok("Help request sent to tutor successfully.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send help request: " + e.getMessage());
        }
    }

    @PostMapping("/ai-suggestion/{taskId}")
    public ResponseEntity<String> getAISuggestion(@PathVariable Long taskId,@RequestBody String message) {
        try {
            if (message == null || message.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Message is required.");
            }

            String aiResponse = taskService.getAISuggestion(taskId, message);
            return ResponseEntity.ok(aiResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("AI Suggestion failed: " + e.getMessage());
        }
    }







}
