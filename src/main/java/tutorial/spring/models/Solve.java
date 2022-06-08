package tutorial.spring.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
@Getter
@Setter
@Entity
public class Solve implements Serializable{
    public Solve(){
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solve solve = (Solve) o;
        return user.equals(solve.user) && variant.equals(solve.variant) && problem.equals(solve.problem) && Objects.equals(answer, solve.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, variant, problem, answer);
    }

    public Solve(User user, Variant variant, Problem problem, String answer,boolean isCorrect,long points) {
        this.user = user;
        this.variant = variant;
        this.problem = problem;
        this.answer = answer;
        this.isCorrect = isCorrect;
        this.points = points;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Variant variant;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Problem problem;
    @Column(nullable = false)
    private String answer;
    @Column(nullable = false)
    private boolean isCorrect;
    @Column(nullable = false)
    private long points;


}
