package tn.esprit.innoxpert.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Task;
import tn.esprit.innoxpert.Entity.TypeStatus;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.TaskRepository;
import tn.esprit.innoxpert.Repository.UserRepository;
import tn.esprit.innoxpert.Util.EmailClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskService implements TaskServiceInterface {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;

    private final EmailClass emailClass = new EmailClass();

    @Value("${huggingface.token}")
    private String huggingFaceToken;







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


    @Override
    @Async
    public void sendHelpRequest(Long taskId, String messageContent) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task with ID: " + taskId + " not found"));

        User student = task.getStudent();
        User tutor = student.getTutor();

        if (tutor == null || tutor.getEmail() == null) {
            throw new NotFoundException("Tutor for student " + student.getFirstName() + " not found or doesn't have an email.");
        }

        String subject = "🆘 Help Request for a Task";

        String body = String.format(
                "Hello %s,\n\n" +
                        "The student %s %s has requested help regarding the following task:\n\n" +
                        "📄 Task Description:\n%s\n\n" +
                        "📝 Student's Message:\n%s\n\n" +
                        "Please respond to assist them.\n\n" +
                        "Best regards,\nInternship Platform",
                tutor.getFirstName(),
                student.getFirstName(),
                student.getLastName(),
                task.getDescription(),
                messageContent
        );

        emailClass.sendEmail(tutor.getEmail(), body, subject);
    }

    @Override
    public String getAISuggestion(Long taskId, String studentMessage) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task with ID: " + taskId + " not found"));

        String taskDescription = task.getDescription();
        String prompt = "You are an assistant. Please provide clear and numbered steps to complete the following task.\n" +
                "Task: " + taskDescription + "\n" +
                "Student Request: " + studentMessage;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(huggingFaceToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("inputs", prompt);

        HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Object[]> response = restTemplate.postForEntity(
                    "https://api-inference.huggingface.co/models/mrm8488/t5-base-finetuned-common_gen"
                    ,request,
                    Object[].class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().length > 0) {
                Map result = (Map) response.getBody()[0];
                return result.get("generated_text").toString();
            } else {
                return "No suggestion available at the moment.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "⚠️ Error while contacting AI copilot: " + e.getMessage();
        }
    }




}
