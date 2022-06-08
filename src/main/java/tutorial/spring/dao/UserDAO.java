package tutorial.spring.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import tutorial.spring.models.Problem;
import tutorial.spring.models.User;

import java.util.List;

@Component
public class UserDAO extends AbstractHibernateDAO<User>{
    public UserDAO(){
        setClazz(User.class);
    }
}
