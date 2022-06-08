package tutorial.spring.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
public class TeacherStudent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    public TeacherStudent(User teacher, User student, boolean accepted,User sender) {
        this.teacher = teacher;
        this.student = student;
        this.accepted = accepted;
        this.sender = sender;
    }

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(nullable = false)
    private boolean accepted;

    public TeacherStudent() {

    }

    @Override
    public String toString() {
        return "TeacherStudent{" +
                "id=" + id +
                ", teacher=" + teacher +
                ", student=" + student +
                ", accepted=" + accepted +
                '}';
    }
}
