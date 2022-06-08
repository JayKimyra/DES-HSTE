package tutorial.spring.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import tutorial.spring.models.Problem;
import tutorial.spring.models.Variant;

import java.util.ArrayList;
import java.util.List;

@Component
public class VariantDAO extends AbstractHibernateDAO<Variant>{
    public VariantDAO(){
        setClazz(Variant.class);
    }

}
