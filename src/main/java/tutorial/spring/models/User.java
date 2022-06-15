package tutorial.spring.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity(name = "users")
public class User implements Serializable {
    public User(){
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String login;
    @Column
    private String password;
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Variant> variants;
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Problem> problems;
    @Column
    String imgUrl = null;
    @Column(columnDefinition="text")
    String info;
    @OneToMany(mappedBy = "teacher")
    private Set<TeacherStudent> students = new HashSet<>();


    @OneToMany(mappedBy = "student")
    private Set<TeacherStudent> teachers = new HashSet<>();



    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
