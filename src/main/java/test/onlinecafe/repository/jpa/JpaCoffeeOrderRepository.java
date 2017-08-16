package test.onlinecafe.repository.jpa;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import test.onlinecafe.model.CoffeeOrder;
import test.onlinecafe.repository.CoffeeOrderRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Profile("repo-jpa")
@Repository
@Transactional(readOnly = true)
public class JpaCoffeeOrderRepository implements CoffeeOrderRepository {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public CoffeeOrder save(CoffeeOrder order) {
        if (order.isNew()) {
            em.persist(order);
            return order;
        } else {
            return em.merge(order);
        }
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return em.createNamedQuery(CoffeeOrder.DELETE).setParameter("id", id).executeUpdate() != 0;
    }

    @Override
    public CoffeeOrder get(int id) {
        return em.find(CoffeeOrder.class, id);
    }

    @Override
    public List<CoffeeOrder> getAll() {
        return em.createNamedQuery(CoffeeOrder.GET_ALL, CoffeeOrder.class).getResultList();
    }
}
