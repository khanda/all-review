package free.review.allreview.repository;

import free.review.allreview.entity.Goods;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface GoodsRepository extends
        PagingAndSortingRepository<Goods, Long>,
        GoodsRepositoryCustom {
    @Query("SELECT u FROM Goods u WHERE u.name = :#{#name}")
    Collection<Goods> findByName(@Param("name") String name);
}
