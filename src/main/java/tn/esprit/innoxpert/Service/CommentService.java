package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Comment;
import tn.esprit.innoxpert.Entity.Comment;
import tn.esprit.innoxpert.Entity.Rating;
import tn.esprit.innoxpert.Repository.CommentRepository;
import tn.esprit.innoxpert.Repository.PostRepository;
import tn.esprit.innoxpert.Repository.RatingRepository;
import tn.esprit.innoxpert.Repository.UserRepository;
import tn.esprit.innoxpert.Util.EmailClass;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentService implements CommentServiceInterface {

    @Autowired
    private CommentRepository commentRepository;





    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SendEmailService sendEmailService;

    private final EmailClass emailClass = new EmailClass();

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> getAllCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }


    @Override
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }



    @Override
    public void removeCommentById(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public Comment updateComment(Comment c) {
        return commentRepository.save(c);
    }

    @Override
    public Comment addCommentAndAffectToPostAndToUser(Long idPost, Comment newComment, Long userId) {
        // Liste des mots interdits (à personnaliser)
        List<String> bannedWords = List.of("insulte1", "insulte2", "grosmot1", "grosmot2");

        // Vérification si le commentaire contient un mot interdit
        if (containsBannedWords(newComment.getContent(), bannedWords)) {
            // Envoi d'un email pour avertir l'utilisateur
            sendEmailToUser(userId, "Your comment contains forbidden words.", "Warning: Forbidden words in your comment");

            // Lancer une exception
            throw new RuntimeException("Your comment contains forbidden words!");
        }

        // Associer le commentaire au post et à l'utilisateur
        newComment.setPost(postRepository.findById(idPost).orElse(null));
        newComment.setUser(userRepository.findById(userId).orElse(null));

        return commentRepository.save(newComment);
    }

    // Fonction pour vérifier si un commentaire contient un mot interdit
    private boolean containsBannedWords(String content, List<String> bannedWords) {
        String lowerCaseContent = content.toLowerCase();
        return bannedWords.stream().anyMatch(lowerCaseContent::contains);
    }

    // Fonction pour envoyer un email à l'utilisateur
    private void sendEmailToUser(Long userId, String body, String subject) {
        // Récupérer l'email de l'utilisateur à partir de la base de données
        String userEmail = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")).getEmail();

        // Envoi de l'email
       emailClass.sendEmail(userEmail, body, subject);
    }



    @Override
    public Comment addCommentToComment(Long parentCommentId, Comment newComment) {
        Comment parentComment = commentRepository.findById(parentCommentId).get();


        newComment.setParentComment(parentComment);
        return commentRepository.save(newComment);
    }




}
