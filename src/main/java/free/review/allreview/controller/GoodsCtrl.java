package free.review.allreview.controller;

import free.review.allreview.ParamHandler.ParamHandler;
import free.review.allreview.entity.Goods;
import free.review.allreview.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/")
public class GoodsCtrl {
    @Autowired
    private GoodsService goodsService;

    // List All
    @RequestMapping(value = "goods", method = RequestMethod.GET)
    public ResponseEntity<Page<Goods>> getAll(@RequestParam Integer page, @RequestParam Integer limit) {
        Pageable pageable = ParamHandler.createPageable(page, limit, new Sort(Sort.Direction.ASC, "name"));
        return goodsService.getAllResponse(pageable);
    }

    // List One
    @RequestMapping(value = "goods/{id}", method = RequestMethod.GET)
    public ResponseEntity<Goods> getOne(@PathVariable Long id) {
        return goodsService.getOneResponse(id);
    }

    // Create New
    @RequestMapping(value = "goods", method = RequestMethod.POST)
    public ResponseEntity<Goods> createNew(@RequestBody Goods contact, HttpServletRequest req) {
        return goodsService.createNew(contact, req);
    }

    // Update with PATCH
    @RequestMapping(value = "goods/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Goods> patchUpdate(@PathVariable Long id, @RequestBody Goods goods) {
        return goodsService.patchUpdate(id, goods);
    }

    // Delete 
    @RequestMapping(value = "goods/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Goods> delete(@PathVariable Long id) {
        return goodsService.delete(id);
    }
}
