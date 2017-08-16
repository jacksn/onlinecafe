package test.onlinecafe.repository.jpa;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import test.onlinecafe.model.ConfigurationItem;
import test.onlinecafe.repository.ConfigurationRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Profile("repo-jpa")
@Repository
@Transactional(readOnly = true)
public class JpaConfigurationRepository implements ConfigurationRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public ConfigurationItem save(ConfigurationItem configurationItem) {
        return em.merge(configurationItem);
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        return em.createNamedQuery(ConfigurationItem.DELETE).setParameter("id", id).executeUpdate() != 0;
    }

    @Override
    public ConfigurationItem get(String id) {
        return em.find(ConfigurationItem.class, id);
    }
}
