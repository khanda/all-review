package free.review.allreview.repository;

import free.review.allreview.entity.GoodsPrice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsPriceRepository extends CrudRepository<GoodsPrice, Long>, GoodsRepositoryCustom {
}
