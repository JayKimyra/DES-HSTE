package tutorial.spring.dao;

import org.springframework.stereotype.Component;
import tutorial.spring.models.TeacherStudent;

@Component
public class TeacherStudentDAO extends AbstractHibernateDAO<TeacherStudent>{
    public TeacherStudentDAO(){
        setClazz(TeacherStudent.class);
    }
}

