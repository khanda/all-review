package free.review.allreview.controller;

import free.review.allreview.entity.Goods;
import free.review.allreview.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Iterable<Goods>> getAllContacts() {
        return goodsService.getAllResponse();
    }

    // List One
    @RequestMapping(value = "goods/{id}", method = RequestMethod.GET)
    public ResponseEntity<Goods> getSingleContact(@PathVariable Long id) {
        return goodsService.getOneResponse(id);
    }

    // Create New
    @RequestMapping(value = "goods", method = RequestMethod.POST)
    public ResponseEntity<Goods> createNewContact(@RequestBody Goods contact, HttpServletRequest req) {
        return goodsService.createNew(contact, req);
    }

    // Update with PUT
    @RequestMapping(value = "goods/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Goods> putUpdateContact(@PathVariable Long id, @RequestBody Goods contact) {
        return goodsService.putUpdate(id, contact);
    }

    // Delete 
    @RequestMapping(value = "goods/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Goods> deleteContact(@PathVariable Long id) {
        return goodsService.delete(id);
    }
}
