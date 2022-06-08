package tutorial.spring.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
@Getter
@Setter
@Entity
public class Variant implements Serializable{
    public Variant(){
    }

    public Variant(Set<Problem> problems, User owner) {
        this.problems = problems;
        this.owner = owner;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Problem> problems;
    @ManyToOne(optional = false)
    private User owner;
    @Column
    String imgUrl = null;
    @Override
    public String toString() {
        return "Variant{" +
                "id=" + id +
                ", problems=" + problems +
                '}';
    }


}
