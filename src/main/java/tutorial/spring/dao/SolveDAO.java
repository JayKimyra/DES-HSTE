package tutorial.spring.dao;

import org.springframework.stereotype.Component;
import tutorial.spring.models.Solve;

@Component
public class SolveDAO extends AbstractHibernateDAO<Solve>{
    public SolveDAO(){
        setClazz(Solve.class);
    }
}

