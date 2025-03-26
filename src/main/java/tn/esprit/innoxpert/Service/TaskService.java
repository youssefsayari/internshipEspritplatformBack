package tn.esprit.innoxpert.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Task;
import tn.esprit.innoxpert.Entity.TypeStatus;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.TaskRepository;
import tn.esprit.innoxpert.Repository.UserRepository;

import java.util.List;

@Service
public class TaskService implements TaskServiceInterface {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(Long TaskId) {
        return taskRepository.findById(TaskId)
                .orElseThrow(() -> new NotFoundException("Task with ID : " + TaskId + " was not found."));
    }


    @Override
    public Task addTask(Task b) {
        b.setStatus(TypeStatus.INPROGRESS);
        return taskRepository.save(b);
    }

    @Override
    public void removeTaskById(Long TaskId) {
        if (!taskRepository.existsById(TaskId)) {
            throw new NotFoundException("Task with ID :  " + TaskId + " was not found.");
        }
        taskRepository.deleteById(TaskId);
    }

    @Override
    public Task updateTask(Task b) {
        if ( !taskRepository.existsById(b.getIdTask())) {
            throw new NotFoundException("Task with ID: " + b.getIdTask() + " was not found. Cannot update.");
        }
        return taskRepository.save(b);
    }

    @Override
    public Task addAndaffectTaskToStudent(Long idUser, Task newTask) {
        User student = userRepository.findById(idUser)
                .orElseThrow(() -> new NotFoundException("User with ID: " + idUser + " not found"));

        newTask.setStudent(student);
        return taskRepository.save(newTask);    }

    @Override
    public Task ChangeTaskStatus(Long idTask, TypeStatus typeStatus) {
        Task task = taskRepository.findById(idTask)
                .orElseThrow(() -> new NotFoundException("Task with ID: " + idTask + " not found"));

        task.setStatus(typeStatus);
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getTasksByUserId(Long idUser) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new NotFoundException("User with ID: " + idUser + " not found"));

        return taskRepository.findByStudent(user);
    }


}
