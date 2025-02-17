package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.Entity.Task;
import tn.esprit.innoxpert.Service.TaskServiceInterface;

import java.util.List;

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






}
