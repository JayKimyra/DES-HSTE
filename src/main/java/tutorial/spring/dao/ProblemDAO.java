package tutorial.spring.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import tutorial.spring.models.Problem;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProblemDAO extends AbstractHibernateDAO<Problem>{
    public ProblemDAO(){
        setClazz(Problem.class);
    }
    public List<Problem> findNRandomsByField(int maxResults, String field,Object value) {
        Session session = getCurrentSession();
        Transaction transaction = session.getTransaction();
        List<Problem> problems = null;
        try{
            session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Problem> criteriaQuery = criteriaBuilder.createQuery(Problem.class);
            Root<Problem> root = criteriaQuery.from(Problem.class);
            criteriaQuery.select(root).orderBy(criteriaBuilder.asc(root.get("id"))).where(criteriaBuilder.equal(root.get(field),value));
            problems = session.createQuery(criteriaQuery).setMaxResults(maxResults).getResultList();
            transaction.commit();
        }
        catch (Exception e){
            transaction.rollback();
            e.printStackTrace();
        }
        finally {
            session.close();
        }
        return problems;
    }
}
