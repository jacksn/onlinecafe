package test.onlinecafe.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import test.onlinecafe.model.CoffeeType;

import java.util.List;

@Transactional(readOnly = true)
public interface JpaCoffeeTypeRepository extends JpaRepository<CoffeeType, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM CoffeeType t WHERE t.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT t FROM CoffeeType t WHERE t.disabled = 'N'")
    List<CoffeeType> getEnabled();
}
