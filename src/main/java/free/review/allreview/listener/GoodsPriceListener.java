package free.review.allreview.listener;

import free.review.allreview.entity.GoodsPrice;

import javax.persistence.PrePersist;

public class GoodsPriceListener {


    @PrePersist
    public void prePersist(GoodsPrice goodsPrice) {
    }
}
