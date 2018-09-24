package free.review.allreview.repository;

import free.review.allreview.entity.DailyBillItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface DailyBillItemRepository extends CrudRepository<DailyBillItem, Long>, DailyBillItemRepositoryCustom {
    @Query("SELECT u FROM DailyBillItem u WHERE u.goodsPrice.id = :#{#goodsId} AND u.customer.id = :#{#customerId}")
    Collection<DailyBillItem> findByGoodsAndCustomer(@Param("customerId") Long customerId, @Param("goodsId") Long goodsId);
}
