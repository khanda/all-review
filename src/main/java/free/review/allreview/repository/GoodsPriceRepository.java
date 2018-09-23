package free.review.allreview.repository;

import free.review.allreview.entity.GoodsPrice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;

@Repository
public interface GoodsPriceRepository extends CrudRepository<GoodsPrice, Long>, GoodsPriceRepositoryCustom {
    @Query("SELECT u FROM GoodsPrice u WHERE u.goods.id = :#{#goodsId} AND u.date = :#{#date}")
    Collection<GoodsPrice> findByGoodsIdAndDate(@Param("goodsId") Long goodsId, @Param("date") Date date);
}
