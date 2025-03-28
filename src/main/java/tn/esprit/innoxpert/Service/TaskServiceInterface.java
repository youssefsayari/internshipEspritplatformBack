package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.Entity.Task;
import tn.esprit.innoxpert.Entity.TypeStatus;
import tn.esprit.innoxpert.Entity.User;

import java.util.List;

public interface TaskServiceInterface {
    public List<Task> getAllTasks();
    public Task getTaskById(Long TaskId);
    public Task addTask(Task b);
    public void removeTaskById(Long TaskId);
    public Task updateTask (Task b );
    public Task addAndaffectTaskToStudent(Long idUser,Task newTask);
    public Task updateAndaffectTaskToStudent(Long idUser, Task task);
    public Task ChangeTaskStatus(Long idTask, TypeStatus typeStatus);
    public List<Task> getTasksByUserId(Long idUser);


}
