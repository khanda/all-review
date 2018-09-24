package free.review.allreview.service;

import free.review.allreview.entity.Customer;
import free.review.allreview.entity.Goods;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface GoodsService {
    ResponseEntity<Iterable<Goods>> getAllResponse();

    ResponseEntity<Goods> getOneResponse(Long id);

    ResponseEntity<Goods> createNew(Goods goods, HttpServletRequest request);

    ResponseEntity<Goods> patchUpdate(Long id, Goods goods);

    ResponseEntity<Goods> delete(Long id);
}
