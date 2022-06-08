package tutorial.spring.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
@Getter
@Setter
@Entity
public class Homework implements Serializable{
    public Homework(){
    }

    public Homework(User student, User teacher, Variant variant, Boolean isArchived,Timestamp timestamp) {
        this.student = student;
        this.teacher = teacher;
        this.variant = variant;
        this.isArchived = isArchived;
        this.timestamp = timestamp;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User student;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User teacher;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Variant variant;
    @Column
    private boolean isArchived;
    @Column
    private Timestamp timestamp;

}
