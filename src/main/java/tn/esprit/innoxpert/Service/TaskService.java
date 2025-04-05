package tn.esprit.innoxpert.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Task;
import tn.esprit.innoxpert.Entity.TypeStatus;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.TaskRepository;
import tn.esprit.innoxpert.Repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Task updateAndaffectTaskToStudent(Long idUser, Task task) {
        User student = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + idUser));

        Task existingTask = taskRepository.findById(task.getIdTask())
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + task.getIdTask()));

        existingTask.setDescription(task.getDescription());
        existingTask.setStatus(task.getStatus());
        existingTask.setStudent(student);

        return taskRepository.save(existingTask);   }

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

    @Override
    public User findStudentWithMostDoneTasks() {
        return this.taskRepository.findStudentWithMostDoneTasks();
    }

    @Override
    public List<User> getStudentsByTutor(User tutor) {
            return userRepository.findByTutor(tutor);


    }

    @Override
    public int countDoneTasksByStudent(Long studentId) {
        User student = userRepository.findById(studentId).get();
        return taskRepository.countDoneTasksByStudent(student);
    }

    @Override
    public Task rateTask(Long taskId, Integer mark) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task with ID: " + taskId + " not found"));

        if (task.getStatus() != TypeStatus.DONE) {
            throw new IllegalStateException("Only DONE tasks can be rated.");
        }

        task.setMark(mark);
        return taskRepository.save(task);
    }



}
