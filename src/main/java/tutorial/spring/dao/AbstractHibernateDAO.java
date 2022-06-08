package tutorial.spring.dao;

import hibernate.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import tutorial.spring.models.Problem;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class AbstractHibernateDAO<T extends Serializable> {
    private Class<T> clazz;

    protected SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public final void setClazz(final Class<T> clazzToSet) {
        clazz = clazzToSet;
    }


    public T findOne(final long id) {
        Session session = getCurrentSession();
        Transaction transaction = session.getTransaction();
        T entity = null;
        try{
            transaction.begin();
            entity = session.get(clazz, id);
            transaction.commit();
        }
        catch (Exception e){
            transaction.rollback();
            e.printStackTrace();
        }
        finally {

            session.close();
        }
        return entity;
    }

    public List<T> findByField(String field,Object value) {
        Session session = getCurrentSession();
        Transaction transaction = session.getTransaction();
        List<T> resultList = null;
        try{
            session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
            Root<T> root = criteriaQuery.from(clazz);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(field),value));
            resultList = session.createQuery(criteriaQuery).getResultList();
            transaction.commit();
        }
        catch (Exception e){
            transaction.rollback();
            e.printStackTrace();
        }
        finally {
            session.close();
        }
        return resultList;
    }

    public List<T> findByFields(Map<String,Object> fields) {
        Session session = getCurrentSession();
        Transaction transaction = session.getTransaction();
        List<T> resultList = null;
        try{
            session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
            Root<T> root = criteriaQuery.from(clazz);
            List<Predicate> predicates = new ArrayList<Predicate>();
            for (String field: fields.keySet()){
                predicates.add(
                        criteriaBuilder.equal(root.get(field), fields.get(field)));
            }
            if (predicates.isEmpty()) throw new Exception("Empty predicates list");
            Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
            criteriaQuery.select(root).where(finalPredicate);
            resultList = session.createQuery(criteriaQuery).getResultList();
            transaction.commit();
        }
        catch (Exception e){
            transaction.rollback();
            e.printStackTrace();
        }
        finally {
            session.close();
        }
        return resultList;
    }
    public T findFirstByFields(Map<String,Object> fields) {
        return findByFields(fields).get(0);
    }
    public List<T> findAll() {
        Session session = getCurrentSession();
        Transaction transaction = session.getTransaction();
        List<T> entities = null;
        try{
            transaction.begin();
            entities = getCurrentSession().createQuery("from " + clazz.getName()).list();
            transaction.commit();
        }
        catch (Exception e){
            transaction.rollback();
            e.printStackTrace();
        }
        finally {
            session.close();
        }
        return entities;
    }
    public List<T> findAllWithLimit(int limit,int offset) {
        Session session = getCurrentSession();
        Transaction transaction = session.getTransaction();
        List<T> entities = null;
        try{
            transaction.begin();
            entities = getCurrentSession().createQuery("from " + clazz.getName()).setMaxResults(limit).setFirstResult(offset).list();
            transaction.commit();
        }
        catch (Exception e){
            transaction.rollback();
            e.printStackTrace();
        }
        finally {
            session.close();
        }
        return entities;
    }


    public T create(final T entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.getTransaction();
        try{
            transaction.begin();
            session.persist(entity);
            transaction.commit();
        }
        catch (Exception e){
            transaction.rollback();
            e.printStackTrace();
        }
        finally {
            session.close();
        }
        return entity;
    }

    public T update(final T entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.getTransaction();
        T updatedEntity = entity;
        try{
            transaction.begin();
            updatedEntity = session.merge(entity);
            transaction.commit();
        }
        catch (Exception e){
            transaction.rollback();
            e.printStackTrace();
        }
        finally {
            session.close();
        }
        return updatedEntity;
    }

    public void delete(final T entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.getTransaction();
        try{
            session.beginTransaction();
            session.remove(entity);
            session.getTransaction().commit();
        }
        catch (Exception e){
            transaction.rollback();
            e.printStackTrace();
        }
        finally {
            session.close();
        }
    }

    public void deleteById(final long entityId) {
        final T entity = findOne(entityId);
        delete(entity);
    }

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
