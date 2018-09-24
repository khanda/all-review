package free.review.allreview.service;

import free.review.allreview.entity.DailyBillItem;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface DailyBillItemService {
    ResponseEntity<Iterable<DailyBillItem>> getAllResponse();

    ResponseEntity<DailyBillItem> getOneResponse(Long id);

    ResponseEntity<DailyBillItem> createNew(DailyBillItem goodsPrice, HttpServletRequest request);

    ResponseEntity<DailyBillItem> patchUpdate(Long id, DailyBillItem dailyBillItem);

    ResponseEntity<DailyBillItem> delete(Long id);
}
