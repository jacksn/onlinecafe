package test.onlinecafe.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import test.onlinecafe.model.ConfigurationItem;

@Transactional(readOnly = true)
public interface JpaConfigurationRepository extends JpaRepository<ConfigurationItem, String> {
    @Modifying
    @Transactional
    @Query("DELETE FROM ConfigurationItem t WHERE t.id=:id")
    int deleteById(@Param("id") String id);
}
