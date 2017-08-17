package test.onlinecafe.repository.jpa;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import test.onlinecafe.model.CoffeeType;
import test.onlinecafe.repository.CoffeeTypeRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Profile("repo-jpa")
@Repository
@Transactional(readOnly = true)
public class JpaCoffeeTypeRepository implements CoffeeTypeRepository {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public CoffeeType save(CoffeeType type) {
        if (!type.isNew() && get(type.getId()) == null) {
            return null;
        }
        if (type.isNew()) {
            em.persist(type);
            return type;
        } else {
            return em.merge(type);
        }
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return em.createNamedQuery(CoffeeType.DELETE).setParameter("id", id).executeUpdate() != 0;
    }

    @Override
    public CoffeeType get(int id) {
        return em.find(CoffeeType.class, id);
    }

    @Override
    public List<CoffeeType> getAll() {
        return em.createNamedQuery(CoffeeType.GET_ALL, CoffeeType.class).getResultList();
    }

    @Override
    public List<CoffeeType> getEnabled() {
        return em.createNamedQuery(CoffeeType.GET_ENABLED, CoffeeType.class).getResultList();
    }

}
