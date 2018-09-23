package free.review.allreview.service;

import free.review.allreview.entity.GoodsPrice;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface GoodsPriceService {
    ResponseEntity<Iterable<GoodsPrice>> getAllResponse();

    ResponseEntity<GoodsPrice> getOneResponse(Long id);

    ResponseEntity<GoodsPrice> createNew(GoodsPrice goodsPrice, HttpServletRequest request);

    ResponseEntity<GoodsPrice> putUpdate(Long id, GoodsPrice goodsPrice);

    ResponseEntity<GoodsPrice> delete(Long id);
}
