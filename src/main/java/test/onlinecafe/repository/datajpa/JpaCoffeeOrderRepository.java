package test.onlinecafe.repository.datajpa;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import test.onlinecafe.model.CoffeeOrder;

@Profile("repo-datajpa")
@Transactional(readOnly = true)
public interface JpaCoffeeOrderRepository extends JpaRepository<CoffeeOrder, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM CoffeeOrder o WHERE o.id=:id")
    int delete(@Param("id") int id);
}
