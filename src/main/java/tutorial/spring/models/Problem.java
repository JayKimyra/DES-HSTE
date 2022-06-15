package tutorial.spring.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.Set;
@Getter
@Setter
@Entity
public class Problem implements Serializable {

    public Problem(){
    }



    public Problem(String title,String text,String solution, String answer, String type, User author, Long maxPoints, String imgUrl) {
        this.title = title;
        this.text = text;
        this.solution = solution;
        this.answer = answer;
        this.type = type;
        this.author = author;
        this.maxPoints = maxPoints;
        this.imgUrl = imgUrl;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String title;
    @Column(columnDefinition = "text")
    private String text;
    @Column(columnDefinition = "text")
    private String solution;
    @Column(columnDefinition = "text")
    private String answer;
    @Column
    private String type;
    @ManyToOne(optional = false,fetch = FetchType.EAGER)
    private User author;
    @Column(nullable = false)
    private Long maxPoints = 1L;
    @Column
    String imgUrl = null;

    @Override
    public String toString() {
        return "Problem{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", answer='" + answer + '\'' +
                ", type='" + type + '\'' +
                ", author=" + author +
                '}';
    }
}
