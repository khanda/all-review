package free.review.allreview.controller;

import free.review.allreview.entity.GoodsPrice;
import free.review.allreview.service.GoodsPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/")
public class GoodsPriceCtrl {
    @Autowired
    private GoodsPriceService goodsPriceService;

    // List All
    @RequestMapping(value = "goodsPrices", method = RequestMethod.GET)
    public ResponseEntity<Iterable<GoodsPrice>> getAll() {
        return goodsPriceService.getAllResponse();
    }

    // List One
    @RequestMapping(value = "goodsPrices/{id}", method = RequestMethod.GET)
    public ResponseEntity<GoodsPrice> getOne(@PathVariable Long id) {
        return goodsPriceService.getOneResponse(id);
    }

    // Create New
    @RequestMapping(value = "goodsPrices", method = RequestMethod.POST)
    public ResponseEntity<GoodsPrice> createNew(@RequestBody GoodsPrice goodsPrice, HttpServletRequest req) {
        return goodsPriceService.createNew(goodsPrice, req);
    }

    // Update with PATCH
    @RequestMapping(value = "goodsPrices/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<GoodsPrice> putUpdate(@PathVariable Long id, @RequestBody GoodsPrice goodsPrice) {
        return goodsPriceService.patchUpdate(id, goodsPrice);
    }

    // Delete 
    @RequestMapping(value = "goodsPrices/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<GoodsPrice> delete(@PathVariable Long id) {
        return goodsPriceService.delete(id);
    }
}
