package tutorial.spring.dao;

import org.springframework.stereotype.Component;
import tutorial.spring.models.Homework;

@Component
public class HomeworkDAO extends AbstractHibernateDAO<Homework>{
    public HomeworkDAO(){
        setClazz(Homework.class);
    }
}

